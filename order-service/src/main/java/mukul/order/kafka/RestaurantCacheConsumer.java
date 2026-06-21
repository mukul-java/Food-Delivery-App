package mukul.order.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mukul.contracts.events.RestaurantCacheEvent;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class RestaurantCacheConsumer {

    private final RedisTemplate<String,String> redisTemplate;

    @KafkaListener(topics = "restaurant-cache-sync", groupId = "order-service")
    public void consume(RestaurantCacheEvent event) {

        String redisKey = "restaurant:" + event.getRestaurantId();
        switch(event.getOperation()) {
            case CREATE:
            case UPDATE:
            case SYNC:
                redisTemplate.opsForValue().set(redisKey, "VALID");
                log.info("Restaurant {} added to redis", event.getRestaurantId());
                break;

            case DELETE:
                redisTemplate.delete(redisKey);
                log.info("Restaurant {} removed from redis", event.getRestaurantId());
                break;
        }
    }
}