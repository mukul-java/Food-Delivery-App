package mukul.paymentservice.statemachine.states;

import org.springframework.stereotype.Component;

import mukul.paymentservice.enums.PaymentStatus;
import mukul.paymentservice.model.Payment;
import mukul.paymentservice.statemachine.PaymentState;

@Component
public class PendingState extends PaymentState {

    @Override
    public PaymentStatus getStatus() {
        return PaymentStatus.PENDING;
    }

    @Override
    public void markSuccess(Payment payment) {
        payment.setStatus(PaymentStatus.SUCCESS);
    }

    @Override
    public void markFailed(Payment payment) {
        payment.setStatus(PaymentStatus.FAILED);
    }

    @Override
    public void cancel(Payment payment) {
        payment.setStatus(PaymentStatus.CANCELLED);
    }

    @Override
    public void expire(Payment payment) {
        payment.setStatus(PaymentStatus.EXPIRED);
    }
}