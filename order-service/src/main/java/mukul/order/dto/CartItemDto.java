package mukul.order.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartItemDto {
    private String foodItemId;
    private String name;
    private int price;
    private int quantity;
    private String specialInstructions;
}
