package mukul.paymentservice.service;

import mukul.contracts.events.OrderCreatedEvent;
import mukul.paymentservice.model.Payment;
import mukul.paymentservice.model.PaymentStatus;
import mukul.paymentservice.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class PaymentService {
    @Autowired
    private PaymentRepository paymentRepository;
    public String processPayment(Payment payment) {
        //TODO: Check for credit card validity
        //TODO: call some 3rd party to handle payment request
        payment.setTimestamp(System.currentTimeMillis());
        payment.setPaymentStatus(PaymentStatus.APPROVED);
        payment.setCreatedAt(new Date());
        payment = paymentRepository.save(payment);

        // send payment info to order-service
        return "payment was " + payment.getPaymentStatus();
    }

    @KafkaListener(
            topics = "order-created",
            groupId = "payment-group"
    )
    public void consume(OrderCreatedEvent event) {
        log.info("Topic: Order-created \n GroupId: payment-group \n Message received: "+event.toString());
        Payment paymentRequest = new Payment();
        paymentRequest.setOrderId(event.getOrderId());
        paymentRequest.setAmount(event.getAmount());
        String success = processPayment(paymentRequest);
        log.info(success);
    }
}
