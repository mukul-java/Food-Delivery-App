package mukul.paymentservice.statemachine.states;

import org.springframework.stereotype.Component;

import mukul.paymentservice.enums.PaymentStatus;
import mukul.paymentservice.model.Payment;
import mukul.paymentservice.statemachine.PaymentState;

@Component
public class CreatedState extends PaymentState {

    @Override
    public PaymentStatus getStatus() {
        return PaymentStatus.CREATED;
    }

    @Override
    public void markPending(Payment payment) {
        payment.setStatus(PaymentStatus.PENDING);
    }
}