package mukul.order.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import mukul.order.model.Address;
import mukul.order.model.OrderStatus;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponse {
    private Long orderNumber;
    private String restaurantName;
    private String userName;
    private List<OrderItemDto> orderItems;
    private BigDecimal totalAmount;
    private Date orderTime;
    private OrderStatus orderStatus;
    private Date expectedDeliveryTime;
    private Address address;
    private String paymentId;
}
