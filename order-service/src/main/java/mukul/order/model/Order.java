package mukul.order.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "orders")
public class Order {
    @Id
    private String id;
    private Long orderNumber;
    private String restaurantId;
    private List<OrderItem> orderItems;
    private BigDecimal totalAmount;
    private Date orderTime;
    private Address address;
    private Date deliveryTime;
    private OrderStatus orderStatus;
    private String paymentId;
    private String userId;
    private Date createdAt;
    private Date updatedAt;
}
