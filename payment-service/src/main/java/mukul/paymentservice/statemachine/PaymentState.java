package mukul.paymentservice.statemachine;

import mukul.paymentservice.enums.PaymentStatus;
import mukul.paymentservice.exception.InvalidStateTransitionException;
import mukul.paymentservice.model.Payment;

public abstract class PaymentState {

    /**
     * Returns the current state represented by this class.
     */
    public abstract PaymentStatus getStatus();

    /**
     * CREATED -> PENDING
     */
    public void markPending(Payment payment) {
        throw invalidTransition(payment, PaymentStatus.PENDING);
    }

    /**
     * PENDING -> SUCCESS
     */
    public void markSuccess(Payment payment) {
        throw invalidTransition(payment, PaymentStatus.SUCCESS);
    }

    /**
     * PENDING -> FAILED
     */
    public void markFailed(Payment payment) {
        throw invalidTransition(payment, PaymentStatus.FAILED);
    }

    /**
     * PENDING -> CANCELLED
     */
    public void cancel(Payment payment) {
        throw invalidTransition(payment, PaymentStatus.CANCELLED);
    }

    /**
     * PENDING -> EXPIRED
     */
    public void expire(Payment payment) {
        throw invalidTransition(payment, PaymentStatus.EXPIRED);
    }

    protected InvalidStateTransitionException invalidTransition(
            Payment payment,
            PaymentStatus targetStatus
    ) {

        return new InvalidStateTransitionException(
                String.format(
                        "Invalid payment state transition from [%s] to [%s]",
                        payment.getStatus(),
                        targetStatus
                )
        );
    }
}