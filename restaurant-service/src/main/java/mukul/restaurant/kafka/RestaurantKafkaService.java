package mukul.restaurant.kafka;

import lombok.RequiredArgsConstructor;
import mukul.contracts.events.RestaurantCacheEvent;
import mukul.contracts.events.RestaurantOperation;
import mukul.restaurant.model.Restaurant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RestaurantKafkaService {

    private static final Logger log = LoggerFactory.getLogger(RestaurantKafkaService.class);
    private final KafkaTemplate<String, RestaurantCacheEvent> kafkaTemplate;
    private static final String TOPIC = "restaurant-cache-sync";

    public void publishRestaurantCreated(Restaurant restaurant) {

        RestaurantCacheEvent event =
                RestaurantCacheEvent.newBuilder()
                        .setRestaurantId(restaurant.getId())
                        .setRestaurantName(restaurant.getName())
                        .setOperation(RestaurantOperation.CREATE)
                        .build();

        kafkaTemplate.send(TOPIC, restaurant.getId(), event);
        log.info("Restaurant create event published successfully: {}", event);
    }

    public void publishRestaurantSync(Restaurant restaurant) {

        RestaurantCacheEvent event =
                RestaurantCacheEvent.newBuilder()
                        .setRestaurantId(restaurant.getId())
                        .setRestaurantName(restaurant.getName())
                        .setOperation(RestaurantOperation.SYNC)
                        .build();

        kafkaTemplate.send(TOPIC, restaurant.getId(), event);
        log.info("Restaurant sync event published successfully: {}", event);
    }

    public void publishRestaurantUpdated(Restaurant restaurant) {

        RestaurantCacheEvent event =
                RestaurantCacheEvent.newBuilder()
                        .setRestaurantId(restaurant.getId())
                        .setRestaurantName(restaurant.getName())
                        .setOperation(RestaurantOperation.UPDATE)
                        .build();

        kafkaTemplate.send(
                TOPIC,
                restaurant.getId(),
                event
        );
    }

    public void publishRestaurantDeleted(String restaurantId) {

        RestaurantCacheEvent event =
                RestaurantCacheEvent.newBuilder()
                        .setRestaurantId(restaurantId)
                        .setRestaurantName("")
                        .setOperation(RestaurantOperation.DELETE)
                        .build();

        kafkaTemplate.send(TOPIC, restaurantId, event);
        log.info("Restaurant delete event published successfully: {}", event);
    }
}