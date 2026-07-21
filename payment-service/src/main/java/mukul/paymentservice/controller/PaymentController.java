package mukul.paymentservice.controller;

import lombok.RequiredArgsConstructor;
import mukul.paymentservice.dto.ApiResponse;
import mukul.paymentservice.dto.PaymentRequestDto;
import mukul.paymentservice.dto.PaymentResponseDto;
import mukul.paymentservice.service.PaymentService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<PaymentResponseDto> createPayment(
            @RequestBody PaymentRequestDto request) {

        PaymentResponseDto response = paymentService.createPayment(request);

        return ApiResponse.<PaymentResponseDto>builder()
                .success(true)
                .message("Payment created successfully.")
                .data(response)
                .build();
    }

    @GetMapping("/{paymentId}")
    public ApiResponse<PaymentResponseDto> getPayment(
            @PathVariable String paymentId) {

        PaymentResponseDto response = paymentService.getPayment(paymentId);

        return ApiResponse.<PaymentResponseDto>builder()
                .success(true)
                .message("Payment fetched successfully.")
                .data(response)
                .build();
    }

    @PatchMapping("/{paymentId}/pending")
    public ApiResponse<PaymentResponseDto> markPending(
            @PathVariable String paymentId) {

        PaymentResponseDto response = paymentService.markPending(paymentId);

        return ApiResponse.<PaymentResponseDto>builder()
                .success(true)
                .message("Payment marked as PENDING.")
                .data(response)
                .build();
    }

    @PatchMapping("/{paymentId}/success")
    public ApiResponse<PaymentResponseDto> markSuccess(
            @PathVariable String paymentId) {

        PaymentResponseDto response = paymentService.markSuccess(paymentId);

        return ApiResponse.<PaymentResponseDto>builder()
                .success(true)
                .message("Payment marked as SUCCESS.")
                .data(response)
                .build();
    }

    @PatchMapping("/{paymentId}/failed")
    public ApiResponse<PaymentResponseDto> markFailed(
            @PathVariable String paymentId) {

        PaymentResponseDto response = paymentService.markFailed(paymentId);

        return ApiResponse.<PaymentResponseDto>builder()
                .success(true)
                .message("Payment marked as FAILED.")
                .data(response)
                .build();
    }

    @PatchMapping("/{paymentId}/cancel")
    public ApiResponse<PaymentResponseDto> cancelPayment(
            @PathVariable String paymentId) {

        PaymentResponseDto response = paymentService.cancelPayment(paymentId);

        return ApiResponse.<PaymentResponseDto>builder()
                .success(true)
                .message("Payment cancelled successfully.")
                .data(response)
                .build();
    }

    @PatchMapping("/{paymentId}/expire")
    public ApiResponse<PaymentResponseDto> expirePayment(
            @PathVariable String paymentId) {

        PaymentResponseDto response = paymentService.expirePayment(paymentId);

        return ApiResponse.<PaymentResponseDto>builder()
                .success(true)
                .message("Payment expired successfully.")
                .data(response)
                .build();
    }
}