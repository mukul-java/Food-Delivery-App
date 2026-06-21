package mukul.restaurant.service;

import lombok.extern.slf4j.Slf4j;
import mukul.contracts.events.OrderCreatedEvent;
import mukul.restaurant.model.FoodItem;
import mukul.restaurant.model.Restaurant;
import mukul.restaurant.repository.FoodItemRepository;
import mukul.restaurant.repository.RestaurantRepository;
import mukul.restaurant.dto.FoodItemDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class FoodItemService {
    @Autowired
    private FoodItemRepository foodItemRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    public FoodItemDto addFoodItem(FoodItemDto foodItemDto, String username ) {

        Restaurant restaurant = restaurantRepository.findById(foodItemDto.getRestaurantId())
                        .orElseThrow(() -> new RuntimeException("Restaurant not found"));
        log.info("Restaurant owner :{}", restaurant.getName());
        log.info("User Name : {}", username);

        if (!restaurant.getOwner().getUsername().equals(username)) {
            throw new RuntimeException("Access Denied. Not restaurant owner");
        }

        FoodItem foodItem = convertToFoodItem(foodItemDto);

        foodItem.setCreatedAt(new Date());
        foodItem.setUpdatedAt(new Date());

        FoodItem savedFoodItem = foodItemRepository.save(foodItem);

        return convertToFoodItemDto(savedFoodItem);
    }

    public List<FoodItemDto> getAllFoodItems( String restaurantId ) {

        return foodItemRepository
                .findByRestaurantId(restaurantId)
                .stream()
                .map(this::convertToFoodItemDto)
                .toList();
    }

    // get all food items in the database.
    public Page<FoodItemDto> getAllFoodItems(int page, int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<FoodItem> foodItems = foodItemRepository.findAll(pageable);
        return foodItems.map(this::convertToFoodItemDto);
    }

    public FoodItemDto updateFoodItem( FoodItemDto foodItemDto, String username ) {

        Restaurant restaurant = restaurantRepository.findById(foodItemDto.getRestaurantId())
                .orElseThrow(() -> new RuntimeException("Restaurant not found"));

        if (!restaurant.getOwner().getUsername().equals(username)) {
            throw new RuntimeException("Access Denied. Not restaurant owner");
        }

        FoodItem foodItem = foodItemRepository.findById(foodItemDto.getId())
                .orElseThrow(() -> new RuntimeException("Food item not found"));

        foodItem.setName(foodItemDto.getName());
        foodItem.setDescription(foodItemDto.getDescription());
        foodItem.setPrice(foodItemDto.getPrice());
        foodItem.setQuantity(foodItemDto.getQuantity());
        foodItem.setUpdatedAt(new Date());

        FoodItem updatedFoodItem = foodItemRepository.save(foodItem);

        return convertToFoodItemDto(updatedFoodItem);
    }

    public void updateFoodItemQuantity(List<String> foodItemIds, List<Integer> orderQuantities) {
        try {
            for (int i = 0; i < foodItemIds.size(); i++) {
                updateFoodItemQuantity(foodItemIds.get(i), orderQuantities.get(i));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void updateFoodItemQuantity(String foodItemId, Integer quantity) {
        Optional<FoodItem> foodItem = foodItemRepository.findById(foodItemId);
        if (foodItem.isPresent()) {
            FoodItem foodItem1 = foodItem.get();
            foodItem1.setQuantity(foodItem1.getQuantity() - quantity);
            foodItem1.setUpdatedAt(new Date());
            foodItemRepository.save(foodItem1);
        }
    }

    //Mappers and helper classes:

    private FoodItemDto convertToFoodItemDto( FoodItem foodItem ) {

        return FoodItemDto.builder()
                .id(foodItem.getId())
                .name(foodItem.getName())
                .description(foodItem.getDescription())
                .price(foodItem.getPrice())
                .quantity(foodItem.getQuantity())
                .restaurantId(foodItem.getRestaurantId())
                .build();
    }

    private FoodItem convertToFoodItem( FoodItemDto dto ) {

        return FoodItem.builder()
                .id(dto.getId())
                .name(dto.getName())
                .description(dto.getDescription())
                .price(dto.getPrice())
                .quantity(dto.getQuantity())
                .restaurantId(dto.getRestaurantId())
                .build();
    }

    @KafkaListener(
            topics = "order-created",
            groupId = "restaurant-group"
    )
    public void createOrder(OrderCreatedEvent event) {
        log.info("Order received at Kafka Listener restaurant-group: "+ event.toString());
        updateFoodItemQuantity(event.getFoodItemIds(), event.getOrderQuantities());
    }
}
