package mukul.order;

import lombok.extern.slf4j.Slf4j;
import mukul.order.services.OrderServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
@Slf4j
public class OrderServiceApplication {

    @Autowired
    private OrderServiceImpl orderService;

    public static void main(String[] args) {
        SpringApplication.run(OrderServiceApplication.class, args);
    }

//    @KafkaListener(topics = "payment-notification-topic")
//    public void receivePaymentNotification(List<String> paymentInfo) {
//        log.info("Received payment notification for order id: {}.", paymentInfo.get(1));
//        orderService.updateOrderAfterPayment(paymentInfo);
//    }
}
