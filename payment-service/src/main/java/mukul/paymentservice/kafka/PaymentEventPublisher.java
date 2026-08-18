package mukul.paymentservice.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mukul.contracts.events.PaymentStatusChangedEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentEventPublisher {

    private final KafkaTemplate<String, PaymentStatusChangedEvent> kafkaTemplate;

    @Value("${kafka.topic.payment-status-changed}")
    private String paymentStatusChangedTopic;

    public void publishPaymentStatusChanged(PaymentStatusChangedEvent event) {
        log.info("Publishing PaymentStatusChangedEvent for paymentId={}", event.getPaymentId());
        kafkaTemplate.send(
                paymentStatusChangedTopic,
                event.getPaymentId(),
                event
        );
        log.info("Published PaymentStatusChangedEvent for paymentId={}", event.getPaymentId());
    }
}