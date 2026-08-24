package mukul.restaurant.service;

import mukul.restaurant.dto.FoodItemDto;
import mukul.restaurant.dto.RestaurantRequestDto;
import mukul.restaurant.dto.RestaurantResponseDto;
import mukul.restaurant.exception.RestaurantNotFound;
import mukul.restaurant.kafka.RestaurantKafkaService;
import mukul.restaurant.model.FoodItem;
import mukul.restaurant.model.Restaurant;
import mukul.restaurant.repository.FoodItemRepository;
import mukul.restaurant.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RestaurantService {
    private static final Logger log = LoggerFactory.getLogger(RestaurantService.class);
    @Autowired
    private RestaurantRepository restaurantRepository;
    @Autowired
    private FoodItemRepository foodItemRepository;
    @Autowired
    private final RestaurantKafkaService restaurantKafkaService;

    public RestaurantResponseDto addRestaurant(RestaurantRequestDto request, String ownerId) {
        String finalOwnerId = resolveOwnerId(ownerId);

        Restaurant restaurant = Restaurant.builder()
                .name(request.getName())
                .description(request.getDescription())
                .address(request.getAddress())
                .contactInfo(request.getContactInfo())
                .rating(0.0)
                .ownerId(finalOwnerId)
                .build();

        restaurant.setCreatedAt(new java.util.Date());
        restaurant.setUpdatedAt(new java.util.Date());

        Restaurant savedRestaurant = restaurantRepository.save(restaurant);
        log.info("Restaurant saved successfully: {}", savedRestaurant);
        restaurantKafkaService.publishRestaurantCreated(savedRestaurant);
        return convertToRestaurantResponseDto(savedRestaurant);
    }

    public RestaurantResponseDto updateRestaurant(
            String id,
            RestaurantRequestDto request,
            String ownerId) {

        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() ->
                        new RestaurantNotFound("Restaurant not found"));

        validateOwnerId(restaurant, ownerId);

        restaurant.setName(request.getName());
        restaurant.setDescription(request.getDescription());
        restaurant.setAddress(request.getAddress());
        restaurant.setContactInfo(request.getContactInfo());
        restaurant.setUpdatedAt(new java.util.Date());

        Restaurant updatedRestaurant =
                restaurantRepository.save(restaurant);

        restaurantKafkaService.publishRestaurantUpdated(updatedRestaurant);

        return convertToRestaurantResponseDto(updatedRestaurant);
    }

    public RestaurantResponseDto getRestaurant(String id) {
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new RestaurantNotFound("Restaurant not found."));

        return convertToRestaurantResponseDto(restaurant);
    }

    public List<RestaurantResponseDto> getAllRestaurants() {
        List<Restaurant> restaurants = restaurantRepository.findAll();
        return restaurants.stream()
                .map(this::convertToRestaurantResponseDto)
                .toList();
    }

    public List<RestaurantResponseDto> getRestaurantsByOwnerId(String ownerId) {
        String finalOwnerId = resolveOwnerId(ownerId);
        List<Restaurant> restaurants = restaurantRepository.findByOwnerId(finalOwnerId);
        return restaurants.stream()
                .map(this::convertToRestaurantResponseDto)
                .toList();
    }

    private void validateOwnerId(Restaurant restaurant, String ownerId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getAuthorities() != null) {
            boolean isAdmin = auth.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equalsIgnoreCase("ROLE_ADMIN") 
                            || a.getAuthority().equalsIgnoreCase("ADMIN"));
            if (isAdmin) return;
        }

        String finalOwnerId = resolveOwnerId(ownerId);
        if (restaurant.getOwnerId() != null && !restaurant.getOwnerId().equalsIgnoreCase(finalOwnerId)) {
            throw new RuntimeException("Access Denied. Not restaurant owner");
        }
    }

    private String resolveOwnerId(String ownerId) {
        if (ownerId != null && !ownerId.isBlank()) {
            return ownerId;
        }
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getName() != null && !auth.getName().equals("anonymousUser")) {
            return auth.getName();
        }
        throw new RuntimeException("Owner ID not found or missing from request");
    }

    // Cache publisher:
    public void publishAllRestaurantsToCacheTopic() {
        List<Restaurant> restaurants = restaurantRepository.findAll();
        restaurants.forEach(restaurantKafkaService::publishRestaurantSync);
    }

    // Helper methods:
    private FoodItemDto convertToFoodItemResponse(FoodItem foodItem) {
       return FoodItemDto.builder()
               .id(foodItem.getId())
               .name(foodItem.getName())
               .description(foodItem.getDescription())
               .price(foodItem.getPrice())
               .quantity(foodItem.getQuantity())
               .restaurantId(foodItem.getRestaurant().getId())
               .build();
    }

    private RestaurantResponseDto convertToRestaurantResponseDto(Restaurant savedRestaurant) {
        List<FoodItemDto> foodItems = foodItemRepository
                        .findByRestaurant_Id(savedRestaurant.getId())
                        .stream()
                        .map(this::convertToFoodItemResponse)
                        .toList();

        return RestaurantResponseDto.builder()
                .id(savedRestaurant.getId())
                .name(savedRestaurant.getName())
                .description(savedRestaurant.getDescription())
                .address(savedRestaurant.getAddress())
                .contactInfo(savedRestaurant.getContactInfo())
                .rating(savedRestaurant.getRating())
                .ownerId(savedRestaurant.getOwnerId())
                .foodItems(foodItems.isEmpty() ? null : foodItems)
                .build();
    }
}

