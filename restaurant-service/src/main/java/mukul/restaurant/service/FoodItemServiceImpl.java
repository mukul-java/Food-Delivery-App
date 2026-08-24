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

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

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
        if (foodItemDto == null || foodItemDto.getRestaurantId() == null || foodItemDto.getRestaurantId().isBlank()) {
            throw new RuntimeException("Restaurant ID must not be null or empty when creating a food item");
        }

        Restaurant restaurant = restaurantRepository.findById(foodItemDto.getRestaurantId())
                .orElseThrow(() -> new RuntimeException("Restaurant not found with ID: " + foodItemDto.getRestaurantId()));

        validateRestaurantOwner(restaurant, username);

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
        if (foodItemDto == null || foodItemDto.getId() == null || foodItemDto.getId().isBlank()) {
            throw new RuntimeException("Food Item ID must not be null or empty");
        }

        FoodItem foodItem = foodItemRepository.findById(foodItemDto.getId())
                .orElseThrow(() -> new RuntimeException("Food Item not found with ID: " + foodItemDto.getId()));

        Restaurant restaurant = null;
        if (foodItemDto.getRestaurantId() != null && !foodItemDto.getRestaurantId().isBlank()) {
            restaurant = restaurantRepository.findById(foodItemDto.getRestaurantId())
                    .orElseThrow(() -> new RuntimeException("Restaurant not found with ID: " + foodItemDto.getRestaurantId()));
        } else {
            restaurant = foodItem.getRestaurant();
        }

        if (restaurant == null) {
            throw new RuntimeException("Associated Restaurant not found for Food Item");
        }

        validateRestaurantOwner(restaurant, username);

        if (foodItemDto.getName() != null) foodItem.setName(foodItemDto.getName());
        if (foodItemDto.getDescription() != null) foodItem.setDescription(foodItemDto.getDescription());
        if (foodItemDto.getPrice() != null) foodItem.setPrice(foodItemDto.getPrice());
        if (foodItemDto.getQuantity() != null) foodItem.setQuantity(foodItemDto.getQuantity());
        foodItem.setUpdatedAt(new Date());

        FoodItem updatedFoodItem = foodItemRepository.save(foodItem);

        return convertToFoodItemDto(updatedFoodItem);
    }

    @Override
    public void deleteFoodItem(String foodItemId, String username) {
        FoodItem foodItem = foodItemRepository.findById(foodItemId)
                .orElseThrow(() -> new RuntimeException("Food Item not found"));

        Restaurant restaurant = foodItem.getRestaurant();
        if (restaurant != null) {
            validateRestaurantOwner(restaurant, username);
        }

        foodItemRepository.delete(foodItem);
    }

    private void validateRestaurantOwner(Restaurant restaurant, String username) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        // 1. Bypass check if logged in user is ADMIN
        if (auth != null && auth.getAuthorities() != null) {
            boolean isAdmin = auth.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equalsIgnoreCase("ROLE_ADMIN") 
                            || a.getAuthority().equalsIgnoreCase("ADMIN"));
            if (isAdmin) {
                return;
            }
        }

        // 2. Fallback to SecurityContext username if header username is missing
        if ((username == null || username.isBlank()) && auth != null && auth.getName() != null && !auth.getName().equals("anonymousUser")) {
            username = auth.getName();
        }

        // 3. If ownerId is not set on restaurant, skip validation
        if (restaurant.getOwnerId() == null || restaurant.getOwnerId().isBlank()) {
            return;
        }

        // 4. Validate matching ownerId, username, or email prefix
        if (username != null && !username.isBlank()) {
            String targetOwnerId = restaurant.getOwnerId().trim().toLowerCase();
            String currentUser = username.trim().toLowerCase();

            boolean isMatch = targetOwnerId.equals(currentUser)
                    || (currentUser.contains("@") && currentUser.split("@")[0].equals(targetOwnerId))
                    || (targetOwnerId.contains("@") && targetOwnerId.split("@")[0].equals(currentUser));

            if (!isMatch) {
                log.warn("Access Denied: Current user/id '{}' does not match restaurant ownerId '{}'", username, restaurant.getOwnerId());
                throw new RuntimeException("Access Denied. Not restaurant owner");
            }
        }
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