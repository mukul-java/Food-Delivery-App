package mukul.order.services;

import lombok.RequiredArgsConstructor;
import mukul.contracts.events.OrderCreatedEvent;
import mukul.order.dto.OrderItemDto;
import mukul.order.dto.OrderRequestDto;
import mukul.order.dto.OrderResponseDto;
import mukul.order.model.Order;
import mukul.order.model.OrderItem;
import mukul.order.model.OrderStatus;
import mukul.order.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private WebClient.Builder webClientBuilder;
    @Autowired
    private final KafkaTemplate<String, OrderCreatedEvent> kafkaTemplate;

public OrderResponseDto createOrder(OrderRequestDto request) {

    Order order = Order.builder()
            .orderNumber(request.getOrderNumber())
            .restaurantId(request.getRestaurantId())
            .orderItems(request.getOrderItems())
            .address(request.getAddress())
            .paymentId(request.getPaymentId())
            .build();

    order.setOrderTime(new Date());
    order.setOrderStatus(OrderStatus.PENDING);
    order.setCreatedAt(new Date());
    order.setTotalAmount(totalAmount(order));

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
    return toResponseDto(createdOrder);
}

    public void updateOrderAfterPayment(List<String> paymentInfo) {
        Optional<Order> optionalOrder = orderRepository.findById(paymentInfo.get(1));
        if(optionalOrder.isPresent()) {
            Order order = optionalOrder.get();
            if(paymentInfo.get(2).equals("SUCCESS")) {
                order.setPaymentId(paymentInfo.get(0));
                order.setOrderStatus(OrderStatus.COMPLETED);
                order.setDeliveryTime(new Date(order.getOrderTime().getTime() + 30 * 60 * 1000));
                order.setUpdatedAt(new Date());
                orderRepository.save(order);

                // send notification to restaurant
                // use kafka
                // send this order to delivery service
            }
            else {
                order.setOrderStatus(OrderStatus.CANCELLED);
                order.setUpdatedAt(new Date());
                orderRepository.save(order);
            }
        }
    }

    public void updateOrderStatus(String orderId, OrderStatus orderStatus) {
        Optional<Order> optionalOrder = orderRepository.findById(orderId);
        if(optionalOrder.isPresent()) {
            Order order = optionalOrder.get();
            order.setOrderStatus(orderStatus);
        }
    }

    private BigDecimal totalAmount(Order order){
        int total =0;
        for(int i=0 ; i< order.getOrderItems().size() ; i++){
            OrderItem item = order.getOrderItems().get(i);
            total += item.getQuantity() * item.getPrice();
        }
       return BigDecimal.valueOf(total);
    }

    public Order toEntity(OrderRequestDto dto) {

        return Order.builder()
                .orderNumber(dto.getOrderNumber())
                .restaurantId(dto.getRestaurantId())
                .orderItems(dto.getOrderItems())
                .totalAmount(dto.getTotalAmount())
                .orderTime(dto.getOrderTime())
                .address(dto.getAddress())
                .deliveryTime(dto.getDeliveryTime())
                .orderStatus(dto.getOrderStatus())
                .paymentId(dto.getPaymentId())
                .build();
    }

    private OrderResponseDto toResponseDto(Order order) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return OrderResponseDto.builder()
                .orderNumber(order.getOrderNumber())
                .userName(username)
                .orderItems(
                        order.getOrderItems().stream()
                                .map(item -> OrderItemDto.builder()
                                        .name(item.getName())
                                        .price(item.getPrice())
                                        .quantity(item.getQuantity())
                                        .build())
                                .toList()
                )
                .totalAmount(order.getTotalAmount())
                .orderTime(order.getOrderTime())
                .orderStatus(order.getOrderStatus())
                .expectedDeliveryTime(order.getDeliveryTime())
                .address(order.getAddress())
                .paymentId(order.getPaymentId())
                .build();
    }
}
