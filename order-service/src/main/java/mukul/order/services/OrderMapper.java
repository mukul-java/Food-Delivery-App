package mukul.order.services;

import mukul.order.dto.OrderItemDto;
import mukul.order.dto.OrderRequestDto;
import mukul.order.dto.OrderResponseDto;
import mukul.order.model.Order;
import mukul.order.model.OrderItem;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderMapper {

    public Order toEntity(OrderRequestDto dto) {

        return Order.builder()
                .orderNumber(dto.getOrderNumber())
                .restaurantId(dto.getRestaurantId())
                .userId(dto.getUserId())
                .paymentId(dto.getPaymentId())
                .address(dto.getAddress())
                .deliveryTime(dto.getDeliveryTime())
                .orderStatus(dto.getOrderStatus())
                .orderTime(dto.getOrderTime())
                .totalAmount(dto.getTotalAmount())
                .orderItems(toOrderItems(dto.getOrderItems()))
                .build();
    }

    public OrderResponseDto toResponseDto(Order order) {

        return OrderResponseDto.builder()
                .orderNumber(order.getOrderNumber())
                .restaurantId(order.getRestaurantId())
                .userId(order.getUserId())
                .paymentId(order.getPaymentId())
                .orderStatus(order.getOrderStatus())
                .orderTime(order.getOrderTime())
                .deliveryTime(order.getDeliveryTime())
                .address(order.getAddress())
                .totalAmount(order.getTotalAmount())
                .orderItems(toOrderItemDtos(order.getOrderItems()))
                .build();
    }

    public List<Order> toEntities(List<OrderRequestDto> dtos) {
        return dtos.stream()
                .map(this::toEntity)
                .toList();
    }

    public List<OrderResponseDto> toResponseDtos(List<Order> orders) {
        return orders.stream()
                .map(this::toResponseDto)
                .toList();
    }

    private List<OrderItem> toOrderItems(List<OrderItemDto> dtos) {

        if (dtos == null) {
            return List.of();
        }

        return dtos.stream()
                .map(this::toOrderItem)
                .toList();
    }

    private List<OrderItemDto> toOrderItemDtos(List<OrderItem> items) {

        if (items == null) {
            return List.of();
        }

        return items.stream()
                .map(this::toOrderItemDto)
                .toList();
    }

    private OrderItem toOrderItem(OrderItemDto dto) {

        return OrderItem.builder()
                .foodItemId(dto.getFoodItemId())
                .name(dto.getName())
                .price(dto.getPrice())
                .quantity(dto.getQuantity())
                .build();
    }

    private OrderItemDto toOrderItemDto(OrderItem item) {

        return OrderItemDto.builder()
                .foodItemId(item.getFoodItemId())
                .name(item.getName())
                .price(item.getPrice())
                .quantity(item.getQuantity())
                .build();
    }
}