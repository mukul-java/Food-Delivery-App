package mukul.restaurant.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.util.backoff.FixedBackOff;

@Slf4j
@Configuration
public class KafkaConsumerConfig {

//    @Bean
//    public DefaultErrorHandler errorHandler() {
//
//        return new DefaultErrorHandler(
//
//                (ConsumerRecord<?, ?> record,
//                 Exception exception) -> {
//
//                    log.error(
//                            "Kafka consumer failed for topic={} value={}",
//                            record.topic(),
//                            record.value(),
//                            exception
//                    );
//                },
//
//                new FixedBackOff(
//                        2000L,
//                        3
//                )
//        );
//    }

    //Dead later queue:
    @Bean
    public DefaultErrorHandler errorHandler(
            KafkaTemplate<Object, Object> template
    ) {

        DeadLetterPublishingRecoverer recoverer =
                new DeadLetterPublishingRecoverer(template);

        DefaultErrorHandler errorHandler =
                new DefaultErrorHandler(
                        recoverer,
                        new FixedBackOff(2000L, 3)
                );

        return errorHandler;
    }
}
