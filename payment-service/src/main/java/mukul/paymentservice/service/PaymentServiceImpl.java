package mukul.paymentservice.service;

import lombok.RequiredArgsConstructor;
import mukul.paymentservice.dto.OrderValidationDto;
import mukul.paymentservice.dto.PaymentQueryDto;
import mukul.paymentservice.dto.PaymentRequestDto;
import mukul.paymentservice.dto.PaymentResponseDto;
import mukul.paymentservice.enums.PaymentStatus;
import mukul.paymentservice.exception.PaymentFailedException;
import mukul.paymentservice.exception.PaymentNotFoundException;
import mukul.paymentservice.model.Payment;
import mukul.paymentservice.repository.PaymentRepository;
import mukul.paymentservice.statemachine.PaymentStateFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentStateFactory paymentStateFactory;
    private final WebClient.Builder webClientBuilder;

    @Override
    public PaymentResponseDto createPayment(PaymentRequestDto request) {

        // Synchronously call order-service to validate order existence and total amount
        OrderValidationDto orderResponse;
        try {
            orderResponse = webClientBuilder.build()
                    .get()
                    .uri("http://order-service/api/v1/order/" + request.getOrderId())
                    .retrieve()
                    .bodyToMono(OrderValidationDto.class)
                    .block();
        } catch (Exception ex) {
            throw new PaymentFailedException("Failed to validate order with orderId " + request.getOrderId() + ": " + ex.getMessage());
        }

        if (orderResponse == null) {
            throw new PaymentFailedException("Order not found with orderId: " + request.getOrderId());
        }

        if (orderResponse.getTotalAmount() == null || orderResponse.getTotalAmount().compareTo(request.getAmount()) != 0) {
            throw new PaymentFailedException("Payment amount (" + request.getAmount() + ") does not match order total amount (" + orderResponse.getTotalAmount() + ")");
        }

        Payment payment = Payment.builder()
                .paymentId(UUID.randomUUID().toString())
                .orderId(request.getOrderId())
                .userId(request.getUserId())
                .amount(request.getAmount())
                .currency(request.getCurrency())
                .paymentMethod(request.getPaymentMethod())
                .gateway(request.getGateway())
                .status(PaymentStatus.CREATED)
                .build();

        Payment savedPayment = paymentRepository.save(payment);

        return mapToResponseDto(savedPayment);
    }

    @Override
    public PaymentResponseDto getPayment(String paymentId) {

        Payment payment = getPaymentEntity(paymentId);

        return mapToResponseDto(payment);
    }

    @Override
    public Page<PaymentResponseDto> getPayments(PaymentQueryDto request) {

        // TODO: Implement dynamic filtering using JPA Specification.

        Pageable pageable = PageRequest.of(
                request.getPage(),
                request.getSize(),
                Sort.by(
                        Sort.Direction.fromString(request.getSortDirection()),
                        request.getSortBy()
                )
        );

        Page<Payment> payments = paymentRepository.findAll(pageable);

        return payments.map(this::mapToResponseDto);
    }

    @Override
    public PaymentResponseDto cancelPayment(String paymentId) {

        Payment payment = getPaymentEntity(paymentId);

        // TODO: Validate current payment status.
        // TODO: Transition payment to CANCELLED using the state machine.
        // TODO: Persist updated payment.
        // TODO: Publish payment status changed event.

        throw new UnsupportedOperationException("Cancel payment is not implemented yet.");
    }

    @Override
    public PaymentResponseDto retryPayment(String paymentId) {

        Payment payment = getPaymentEntity(paymentId);

        // TODO: Validate current payment status.
        // TODO: Initiate a new payment attempt through the payment gateway.
        // TODO: Persist updated payment details.
        // TODO: Publish payment status changed event.

        throw new UnsupportedOperationException("Retry payment is not implemented yet.");
    }

    @Override
    public void handleWebhook(String payload) {

        // TODO: Verify webhook signature.
        // TODO: Parse gateway payload.
        // TODO: Retrieve payment using transaction/order reference.
        // TODO: Perform state transition.
        // TODO: Persist updated payment.
        // TODO: Publish payment status changed event.

        throw new UnsupportedOperationException("Webhook handling is not implemented yet.");
    }

    private Payment getPaymentEntity(String paymentId) {
        return paymentRepository.findByPaymentId(paymentId)
                .orElseThrow(() ->
                        new PaymentNotFoundException(
                                "Payment not found with paymentId : " + paymentId
                        ));
    }

    private PaymentResponseDto mapToResponseDto(Payment payment) {

        return PaymentResponseDto.builder()
                .paymentId(payment.getPaymentId())
                .orderId(payment.getOrderId())
                .userId(payment.getUserId())
                .amount(payment.getAmount())
                .currency(payment.getCurrency())
                .status(payment.getStatus())
                .createdAt(payment.getCreatedAt())
                .updatedAt(payment.getUpdatedAt())
                .build();
    }
}