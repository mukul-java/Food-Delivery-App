package mukul.paymentservice.repository;

import mukul.paymentservice.enums.PaymentStatus;
import mukul.paymentservice.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findByPaymentId(String paymentId);
    Optional<Payment> findByOrderId(String orderId);
    List<Payment> findByUserId(String userId);
    List<Payment> findByStatus(PaymentStatus status);
    Optional<Payment> findByGatewayPaymentId(String gatewayPaymentId);
}
