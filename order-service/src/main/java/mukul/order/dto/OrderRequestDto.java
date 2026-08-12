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
public class OrderRequestDto {
        private Long orderNumber;
        private String restaurantId;
        private List<OrderItemDto> orderItems;
        private BigDecimal totalAmount;
        private Date orderTime;
        private Address address;
        private Date deliveryTime;
        private OrderStatus orderStatus;
        private String paymentId;
        private String userId;
        private long totalOrders;
        private long deliveredOrders;
        private long pendingOrders;
        private long cancelledOrders;
        private BigDecimal totalRevenue;
}