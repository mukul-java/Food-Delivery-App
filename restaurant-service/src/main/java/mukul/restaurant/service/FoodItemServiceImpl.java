package mukul.restaurant.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mukul.contracts.events.OrderCreatedEvent;
import mukul.restaurant.dto.FoodItemDto;
import mukul.restaurant.model.FoodItem;
import mukul.restaurant.model.Restaurant;
import mukul.restaurant.repository.FoodItemRepository;
import mukul.restaurant.repository.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FoodItemServiceImpl implements FoodItemService {

    @Autowired
    private FoodItemRepository foodItemRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Override
    public FoodItemDto addFoodItem(FoodItemDto foodItemDto, String username) {

        Restaurant restaurant = restaurantRepository.findById(foodItemDto.getRestaurantId())
                .orElseThrow(() -> new RuntimeException("Restaurant not found"));

        if (!restaurant.getOwner().getUsername().equals(username)) {
            throw new RuntimeException("Access Denied. Not restaurant owner");
        }

        FoodItem foodItem = convertToFoodItem(foodItemDto);

        // JPA relationship
        foodItem.setRestaurant(restaurant);

        foodItem.setCreatedAt(new Date());
        foodItem.setUpdatedAt(new Date());

        FoodItem savedFoodItem = foodItemRepository.save(foodItem);

        return convertToFoodItemDto(savedFoodItem);
    }

    @Override
    public List<FoodItemDto> getAllFoodItems(String restaurantId) {

        return foodItemRepository.findByRestaurant_Id(restaurantId)
                .stream()
                .map(this::convertToFoodItemDto)
                .toList();
    }

    @Override
    public Page<FoodItemDto> getAllFoodItems(int page, int size) {

        Pageable pageable = PageRequest.of(page, size);

        return foodItemRepository.findAll(pageable)
                .map(this::convertToFoodItemDto);
    }

    @Override
    public FoodItemDto updateFoodItem(FoodItemDto foodItemDto, String username) {

        Restaurant restaurant = restaurantRepository.findById(foodItemDto.getRestaurantId())
                .orElseThrow(() -> new RuntimeException("Restaurant not found"));

        if (!restaurant.getOwner().getUsername().equals(username)) {
            throw new RuntimeException("Access Denied. Not restaurant owner");
        }

        FoodItem foodItem = foodItemRepository.findById(foodItemDto.getId())
                .orElseThrow(() -> new RuntimeException("Food Item not found"));

        foodItem.setName(foodItemDto.getName());
        foodItem.setDescription(foodItemDto.getDescription());
        foodItem.setPrice(foodItemDto.getPrice());
        foodItem.setQuantity(foodItemDto.getQuantity());
        foodItem.setUpdatedAt(new Date());

        FoodItem updatedFoodItem = foodItemRepository.save(foodItem);

        return convertToFoodItemDto(updatedFoodItem);
    }

    @Override
    public void updateFoodItemQuantity(List<String> foodItemIds,
                                       List<Integer> orderQuantities) {

        for (int i = 0; i < foodItemIds.size(); i++) {
            updateFoodItemQuantity(foodItemIds.get(i), orderQuantities.get(i));
        }
    }
    private void updateFoodItemQuantity(String foodItemId, Integer quantity) {

        foodItemRepository.findById(foodItemId)
                .ifPresent(foodItem -> {

                    foodItem.setQuantity(foodItem.getQuantity() - quantity);
                    foodItem.setUpdatedAt(new Date());

                    foodItemRepository.save(foodItem);
                });
    }

    // ============================================================
    // Mapper Methods
    // ============================================================

    private FoodItemDto convertToFoodItemDto(FoodItem foodItem) {

        return FoodItemDto.builder()
                .id(foodItem.getId())
                .name(foodItem.getName())
                .description(foodItem.getDescription())
                .price(foodItem.getPrice())
                .quantity(foodItem.getQuantity())
                .restaurantId(foodItem.getRestaurant().getId())
                .build();
    }

    private FoodItem convertToFoodItem(FoodItemDto dto) {

        return FoodItem.builder()
                .id(dto.getId())
                .name(dto.getName())
                .description(dto.getDescription())
                .price(dto.getPrice())
                .quantity(dto.getQuantity())
                .build();
    }

    // ============================================================
    // Kafka Listener
    // ============================================================

    @KafkaListener(
            topics = "order-created",
            groupId = "restaurant-group"
    )
    public void createOrder(OrderCreatedEvent event) {

        log.info("Order received at restaurant-service: {}", event);

        updateFoodItemQuantity(
                event.getFoodItemIds(),
                event.getOrderQuantities()
        );
    }

}