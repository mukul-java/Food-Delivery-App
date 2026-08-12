package mukul.order.dto;

import lombok.*;
import mukul.order.model.Address;
import mukul.order.model.OrderStatus;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponseDto {
    private Long orderNumber;
    private String restaurantId;
    private String userId;
    private List<OrderItemDto> orderItems;
    private BigDecimal totalAmount;
    private Date orderTime;
    private Date deliveryTime;
    private OrderStatus orderStatus;
    private Address address;
    private String paymentId;
}