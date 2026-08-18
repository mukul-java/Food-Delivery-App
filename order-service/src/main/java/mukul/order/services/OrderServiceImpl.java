package mukul.order.services;

import lombok.RequiredArgsConstructor;
import mukul.contracts.events.OrderCreatedEvent;
import mukul.order.dto.OrderRequestDto;
import mukul.order.dto.OrderResponseDto;
import mukul.order.model.Order;
import mukul.order.model.OrderItem;
import mukul.order.model.OrderStatus;
import mukul.order.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderServiceImpl {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private WebClient.Builder webClientBuilder;

    @Autowired
    private KafkaTemplate<String, OrderCreatedEvent> kafkaTemplate;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private CartService cartService;

    public OrderResponseDto checkoutCart(mukul.order.dto.CheckoutRequestDto request) {
        mukul.order.dto.CartDto cart = cartService.getCart(request.getUserId());
        
        if (cart == null || cart.getItems().isEmpty()) {
            throw new RuntimeException("Cart is empty for user: " + request.getUserId());
        }

        Order order = new Order();
        order.setUserId(cart.getUserId());
        order.setRestaurantId(cart.getRestaurantId());
        order.setOrderTime(new Date());
        order.setCreatedAt(new Date());
        order.setOrderStatus(OrderStatus.PENDING);
        order.setAddress(request.getAddress());
        
        List<OrderItem> orderItems = cart.getItems().stream().map(cartItem -> {
            OrderItem item = new OrderItem();
            item.setFoodItemId(cartItem.getFoodItemId());
            item.setName(cartItem.getName());
            item.setPrice(cartItem.getPrice());
            item.setQuantity(cartItem.getQuantity());
            return item;
        }).toList();
        
        order.setOrderItems(orderItems);
        order.setTotalAmount(cart.getTotalAmount());

        Order createdOrder = orderRepository.save(order);

        List<String> foodItemIds = createdOrder.getOrderItems()
                .stream()
                .map(OrderItem::getFoodItemId)
                .toList();

        List<Integer> orderQuantities = createdOrder.getOrderItems()
                .stream()
                .map(OrderItem::getQuantity)
                .toList();

        OrderCreatedEvent event = new OrderCreatedEvent(
                foodItemIds,
                orderQuantities,
                createdOrder.getRestaurantId(),
                createdOrder.getId(),
                createdOrder.getTotalAmount(),
                createdOrder.getUserId()
        );

        kafkaTemplate.send("order-created", event);

        cartService.clearCart(request.getUserId());

        return orderMapper.toResponseDto(createdOrder);
    }

    public OrderResponseDto createOrder(OrderRequestDto request) {

        Order order = orderMapper.toEntity(request);
        order.setOrderTime(new Date());
        order.setCreatedAt(new Date());
        order.setOrderStatus(OrderStatus.PENDING);
        order.setTotalAmount(calculateTotalAmount(order));

        Order createdOrder = orderRepository.save(order);

        List<String> foodItemIds = createdOrder.getOrderItems()
                .stream()
                .map(OrderItem::getFoodItemId)
                .toList();

        List<Integer> orderQuantities = createdOrder.getOrderItems()
                .stream()
                .map(OrderItem::getQuantity)
                .toList();

        OrderCreatedEvent event = new OrderCreatedEvent(
                foodItemIds,
                orderQuantities,
                createdOrder.getRestaurantId(),
                createdOrder.getId(),
                createdOrder.getTotalAmount(),
                createdOrder.getUserId()
        );

        kafkaTemplate.send("order-created", event);

        return orderMapper.toResponseDto(createdOrder);
    }

    public void updateOrderAfterPayment(List<String> paymentInfo) {

        orderRepository.findById(paymentInfo.get(1))
                .ifPresent(order -> {

                    order.setPaymentId(paymentInfo.get(0));

                    if ("SUCCESS".equals(paymentInfo.get(2))) {

                        order.setOrderStatus(OrderStatus.COMPLETED);

                        order.setDeliveryTime(
                                new Date(order.getOrderTime().getTime() + (30 * 60 * 1000))
                        );

                        // Publish Restaurant Event
                        // Publish Delivery Event

                    } else {

                        order.setOrderStatus(OrderStatus.CANCELLED);
                    }

                    order.setUpdatedAt(new Date());

                    orderRepository.save(order);
                });
    }

    public void updateOrderStatus(String orderId, OrderStatus orderStatus) {

        orderRepository.findById(orderId)
                .ifPresent(order -> {

                    order.setOrderStatus(orderStatus);
                    order.setUpdatedAt(new Date());

                    orderRepository.save(order);
                });
    }

    public Page<OrderResponseDto> getAllOrders(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return orderRepository.findAll(pageable).map(orderMapper::toResponseDto);
    }

    public OrderResponseDto getOrderById(String orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));
        return orderMapper.toResponseDto(order);
    }

    public OrderRequestDto getOrderStats() {
        long totalOrders = orderRepository.count();
        long deliveredOrders = orderRepository.countByOrderStatus(OrderStatus.DELIVERED);
        long pendingOrders = orderRepository.countByOrderStatus(OrderStatus.PENDING);
        long cancelledOrders = orderRepository.countByOrderStatus(OrderStatus.CANCELLED);
        BigDecimal totalRevenue = orderRepository.sumTotalAmount();

        if (totalRevenue == null) {
            totalRevenue = BigDecimal.ZERO;
        }

        OrderRequestDto dto = new OrderRequestDto();
        dto.setTotalOrders(totalOrders);
        dto.setDeliveredOrders(deliveredOrders);
        dto.setPendingOrders(pendingOrders);
        dto.setCancelledOrders(cancelledOrders);
        dto.setTotalRevenue(totalRevenue);
        return dto;
    }

    private BigDecimal calculateTotalAmount(Order order) {

        return BigDecimal.valueOf(
                order.getOrderItems()
                        .stream()
                        .mapToInt(item -> item.getPrice() * item.getQuantity())
                        .sum()
        );
    }

    /**
     * Synchronously fetches payment details from payment-service via Eureka and WebClient.
     */
    public Object getPaymentStatusFromPaymentService(String paymentId) {
        return webClientBuilder.build()
                .get()
                .uri("http://payment-service/api/v1/payments/" + paymentId)
                .retrieve()
                .bodyToMono(Object.class)
                .block();
    }
}