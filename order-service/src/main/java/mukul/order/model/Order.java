package mukul.order.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "orders")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private Long orderNumber;

    private String restaurantId;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "order_id")
    private List<OrderItem> orderItems;

    private BigDecimal totalAmount;

    @Temporal(TemporalType.TIMESTAMP)
    private Date orderTime;

    @Embedded
    private Address address;

    @Temporal(TemporalType.TIMESTAMP)
    private Date deliveryTime;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    private String paymentId;

    private String userId;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;
}