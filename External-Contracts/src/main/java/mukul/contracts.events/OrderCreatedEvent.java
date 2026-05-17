package mukul.contracts.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
//Fat event / rich event
public class OrderCreatedEvent {
    private List<String> foodItemIds;
    private List<Integer> orderQuantities;
    //
    private String orderId;
    private BigDecimal amount;
    private String userId;
}
