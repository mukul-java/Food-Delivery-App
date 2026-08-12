package mukul.order.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "order_items",
        indexes = {
                @Index(name = "idx_order_item_food_id", columnList = "food_item_id")
        })
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "food_item_id", nullable = false)
    private String foodItemId;

    @Column(name = "food_name", nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer price;

    @Column(nullable = false)
    private Integer quantity;
}