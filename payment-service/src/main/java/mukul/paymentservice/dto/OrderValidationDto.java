package mukul.paymentservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderValidationDto {

    private Long orderNumber;
    private String restaurantId;
    private String userId;
    private BigDecimal totalAmount;
    private String orderStatus;
}
