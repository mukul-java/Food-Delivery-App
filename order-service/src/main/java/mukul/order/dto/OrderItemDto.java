package mukul.order.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemDto {
    private String foodItemId;
    private String name;
    private Integer price;
    private Integer quantity;
}