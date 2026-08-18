package mukul.paymentservice.statemachine.states;

import org.springframework.stereotype.Component;

import mukul.paymentservice.enums.PaymentStatus;
import mukul.paymentservice.statemachine.PaymentState;

@Component
public class SuccessState extends PaymentState {

    @Override
    public PaymentStatus getStatus() {
        return PaymentStatus.SUCCESS;
    }
}