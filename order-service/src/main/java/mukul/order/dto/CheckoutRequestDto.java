package mukul.order.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import mukul.order.model.Address;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CheckoutRequestDto {
    private String userId;
    private Address address;
}
