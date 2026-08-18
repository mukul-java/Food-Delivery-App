package mukul.paymentservice.dto;

import mukul.paymentservice.enums.Currency;
import mukul.paymentservice.enums.PaymentGateway;
import mukul.paymentservice.enums.PaymentMethod;
import mukul.paymentservice.enums.PaymentStatus;
import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class PaymentResponseDto {
    private String paymentId;
    private String orderId;
    private String userId;
    private BigDecimal amount;
    private Currency currency;
    private PaymentMethod paymentMethod;
    private PaymentGateway gateway;
    private PaymentStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}