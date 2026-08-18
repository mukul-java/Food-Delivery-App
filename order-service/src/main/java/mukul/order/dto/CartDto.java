package mukul.order.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartDto {
    private String cartId;
    private String userId;
    private String restaurantId;
    
    @Builder.Default
    private List<CartItemDto> items = new ArrayList<>();
    
    private BigDecimal totalAmount;
    private Date updatedAt;
}
