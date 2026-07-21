package mukul.paymentservice.service;

import mukul.paymentservice.dto.PaymentRequestDto;
import mukul.paymentservice.dto.PaymentResponseDto;

public interface PaymentService {

    /**
     * Creates a new payment.
     */
    PaymentResponseDto createPayment(PaymentRequestDto request);

    /**
     * Returns payment details.
     */
    PaymentResponseDto getPayment(String paymentId);

    /**
     * CREATED -> PENDING
     */
    PaymentResponseDto markPending(String paymentId);

    /**
     * PENDING -> SUCCESS
     */
    PaymentResponseDto markSuccess(String paymentId);

    /**
     * PENDING -> FAILED
     */
    PaymentResponseDto markFailed(String paymentId);

    /**
     * PENDING -> CANCELLED
     */
    PaymentResponseDto cancelPayment(String paymentId);

    /**
     * PENDING -> EXPIRED
     */
    PaymentResponseDto expirePayment(String paymentId);

}