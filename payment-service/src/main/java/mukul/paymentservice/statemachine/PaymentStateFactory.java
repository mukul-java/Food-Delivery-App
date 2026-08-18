package mukul.paymentservice.statemachine;

import mukul.paymentservice.enums.PaymentStatus;
import mukul.paymentservice.exception.InvalidStateTransitionException;
import mukul.paymentservice.exception.StateNotFoundException;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Component
public class PaymentStateFactory {

    private final Map<PaymentStatus, PaymentState> stateMap;

    public PaymentStateFactory(List<PaymentState> paymentStates) {

        this.stateMap = new EnumMap<>(PaymentStatus.class);

        for (PaymentState state : paymentStates) {
            stateMap.put(state.getStatus(), state);
        }
    }

    public PaymentState getState(PaymentStatus status) {

        PaymentState paymentState = stateMap.get(status);

        if (paymentState == null) {
            throw new StateNotFoundException(
                    "No state implementation found for status : " + status
            );
        }

        return paymentState;
    }
}