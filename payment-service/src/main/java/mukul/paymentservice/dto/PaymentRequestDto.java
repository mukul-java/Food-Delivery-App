package mukul.paymentservice.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import mukul.paymentservice.enums.Currency;
import mukul.paymentservice.enums.PaymentGateway;
import mukul.paymentservice.enums.PaymentMethod;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequestDto {

    @NotNull
    private String orderId;

    @NotNull
    private String userId;

    @NotNull
    @Positive
    private BigDecimal amount;

    @NotNull
    private Currency currency;

    @NotNull
    private PaymentMethod paymentMethod;

    @NotNull
    private PaymentGateway gateway;

    @Valid
    @NotNull
    private CreditCardDto creditCard;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreditCardDto {

        @NotBlank
        private String cardNumber;

        @NotBlank
        private String cardHolderName;

        @NotBlank
        private String expiryMonth;

        @NotBlank
        private String expiryYear;

        @NotBlank
        private String securityCode;
    }
}