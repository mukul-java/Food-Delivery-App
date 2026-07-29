package mukul.restaurant.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "restaurants",
        indexes = {
                @Index(name = "idx_restaurant_name", columnList = "name")
        }
)
public class Restaurant {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", length = 1000)
    private String description;

    @Embedded
    private Address address;

    @Builder.Default
    @ElementCollection
    @CollectionTable(
            name = "restaurant_contacts",
            joinColumns = @JoinColumn(name = "restaurant_id")
    )
    @Column(name = "contact_number")
    private List<Long> contactInfo = new ArrayList<>();

    @Builder.Default
    @OneToMany(
            mappedBy = "restaurant",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<FoodItem> foodItems = new ArrayList<>();

    @Column(name = "rating")
    private Double rating;

    @Embedded
    private OwnerInfo owner;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false)
    private Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at")
    private Date updatedAt;
}