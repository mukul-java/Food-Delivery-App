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
        name = "delivery_agents",
        indexes = {
                @Index(name = "idx_delivery_agents_phone", columnList = "phone_number", unique = true)
        }
)
public class DeliveryAgent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "address", column = @Column(name = "address")),
            @AttributeOverride(name = "city", column = @Column(name = "city")),
            @AttributeOverride(name = "state", column = @Column(name = "state")),
            @AttributeOverride(name = "zipcode", column = @Column(name = "zipcode"))
    })
    private Address address;

    @Column(name = "phone_number", nullable = false, unique = true)
    private Long phoneNumber;
}