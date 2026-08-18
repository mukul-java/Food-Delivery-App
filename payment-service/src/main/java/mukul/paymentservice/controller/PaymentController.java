package mukul.paymentservice.controller;

import lombok.RequiredArgsConstructor;
import mukul.paymentservice.dto.ApiResponse;
import mukul.paymentservice.dto.PaymentQueryDto;
import mukul.paymentservice.dto.PaymentRequestDto;
import mukul.paymentservice.dto.PaymentResponseDto;
import mukul.paymentservice.enums.PaymentGateway;
import mukul.paymentservice.enums.PaymentMethod;
import mukul.paymentservice.enums.PaymentStatus;
import mukul.paymentservice.service.PaymentService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<PaymentResponseDto> createPayment(
            @RequestBody PaymentRequestDto request) {

        return ApiResponse.<PaymentResponseDto>builder()
                .success(true)
                .message("Payment created successfully.")
                .data(paymentService.createPayment(request))
                .build();
    }

    @GetMapping("/{paymentId}")
    public ApiResponse<PaymentResponseDto> getPayment(
            @PathVariable String paymentId) {

        return ApiResponse.<PaymentResponseDto>builder()
                .success(true)
                .message("Payment fetched successfully.")
                .data(paymentService.getPayment(paymentId))
                .build();
    }

    @GetMapping
    public ApiResponse<Page<PaymentResponseDto>> getPayments(
            @ModelAttribute PaymentQueryDto request) {

        return ApiResponse.<Page<PaymentResponseDto>>builder()
                .success(true)
                .message("Payments fetched successfully.")
                .data(paymentService.getPayments(request))
                .build();
    }

    @PostMapping("/{paymentId}/retry")
    public ApiResponse<PaymentResponseDto> retryPayment(
            @PathVariable String paymentId) {

        return ApiResponse.<PaymentResponseDto>builder()
                .success(true)
                .message("Payment retry initiated successfully.")
                .data(paymentService.retryPayment(paymentId))
                .build();
    }

    @PostMapping("/{paymentId}/cancel")
    public ApiResponse<PaymentResponseDto> cancelPayment(
            @PathVariable String paymentId) {

        return ApiResponse.<PaymentResponseDto>builder()
                .success(true)
                .message("Payment cancelled successfully.")
                .data(paymentService.cancelPayment(paymentId))
                .build();
    }

    @PostMapping("/webhook")
    @ResponseStatus(HttpStatus.OK)
    public void handleWebhook(@RequestBody String payload) {
        paymentService.handleWebhook(payload);
    }
}