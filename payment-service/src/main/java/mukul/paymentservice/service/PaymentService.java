package mukul.paymentservice.service;

import mukul.paymentservice.dto.PaymentQueryDto;
import mukul.paymentservice.dto.PaymentRequestDto;
import mukul.paymentservice.dto.PaymentResponseDto;
import mukul.paymentservice.model.Payment;
import org.springframework.data.domain.Page;

public interface PaymentService {

    /** Creates a new payment. */
    PaymentResponseDto createPayment(PaymentRequestDto request);

    /** Returns a payment by payment id. */
    PaymentResponseDto getPayment(String paymentId);

    /** Returns payments matching the given filters. */
    Page<PaymentResponseDto> getPayments(PaymentQueryDto request);

    /** Retries a failed or expired payment. */
    PaymentResponseDto retryPayment(String paymentId);

    /** Cancels a pending payment. */
    PaymentResponseDto cancelPayment(String paymentId);

    /** Processes gateway webhook events. */
    void handleWebhook(String payload);

    /** Publishes a payment status change event. */
//    void publishPaymentStatusChangedEvent(Payment payment);
}