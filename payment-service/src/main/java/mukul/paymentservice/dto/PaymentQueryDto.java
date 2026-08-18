package mukul.paymentservice.dto;

import lombok.Getter;
import lombok.Setter;
import mukul.paymentservice.enums.PaymentGateway;
import mukul.paymentservice.enums.PaymentMethod;
import mukul.paymentservice.enums.PaymentStatus;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Getter
@Setter
public class PaymentQueryDto {

    private String paymentId;

    private String orderId;

    private String userId;

    private PaymentStatus paymentStatus;

    private PaymentMethod paymentMethod;

    private PaymentGateway paymentGateway;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime fromDate;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime toDate;

    private Integer page = 0;

    private Integer size = 10;

    private String sortBy = "createdAt";

    private String sortDirection = "DESC";
}