package mukul.deliveryservice.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "delivery_assignments",
        indexes = {
                @Index(name = "idx_delivery_assignment_order", columnList = "order_number"),
                @Index(name = "idx_delivery_assignment_agent", columnList = "delivery_agent_phone_number")
        }
)
public class DeliveryAssignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "delivery_agent_name")
    private String deliveryAgentName;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "address", column = @Column(name = "agent_address")),
            @AttributeOverride(name = "city", column = @Column(name = "agent_city")),
            @AttributeOverride(name = "state", column = @Column(name = "agent_state")),
            @AttributeOverride(name = "zipcode", column = @Column(name = "agent_zipcode"))
    })
    private Address deliveryAgentCurrentAddress;

    @Column(name = "delivery_agent_phone_number")
    private Long deliveryAgentPhoneNumber;

    @Column(name = "restaurant_name")
    private String restaurantName;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "address", column = @Column(name = "restaurant_address")),
            @AttributeOverride(name = "city", column = @Column(name = "restaurant_city")),
            @AttributeOverride(name = "state", column = @Column(name = "restaurant_state")),
            @AttributeOverride(name = "zipcode", column = @Column(name = "restaurant_zipcode"))
    })
    private Address restaurantAddress;

    @Column(name = "customer_name")
    private String customerName;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "address", column = @Column(name = "customer_address")),
            @AttributeOverride(name = "city", column = @Column(name = "customer_city")),
            @AttributeOverride(name = "state", column = @Column(name = "customer_state")),
            @AttributeOverride(name = "zipcode", column = @Column(name = "customer_zipcode"))
    })
    private Address customerAddress;

    @Column(name = "customer_phone_number")
    private Long customerPhoneNumber;

    @Column(name = "order_number")
    private Long orderNumber;

    @Column(name = "delivery_time")
    private Long deliveryTime;
}