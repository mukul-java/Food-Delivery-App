package mukul.paymentservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.math.BigDecimal;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "payments")
public class Payment {
    @Id
    private String id;
    private Long timestamp;
    private BigDecimal amount;
    private CreditCardInfo creditCardInfo;
    private String orderId;
    private PaymentStatus paymentStatus;
    private Date createdAt;
    private Date updatedAt;

}
