package mukul.restaurant.service;

import mukul.restaurant.dto.FoodItemDto;
import mukul.restaurant.dto.RestaurantRequestDto;
import mukul.restaurant.dto.RestaurantResponseDto;
import mukul.restaurant.exception.RestaurantNotFound;
import mukul.restaurant.kafka.RestaurantKafkaService;
import mukul.restaurant.model.FoodItem;
import mukul.restaurant.model.OwnerInfo;
import mukul.restaurant.model.Restaurant;
import mukul.restaurant.repository.FoodItemRepository;
import mukul.restaurant.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

//    // Eureka http caller
//    @Autowired
//    private WebClient.Builder webClientBuilder;
//    private final String ROLE = "RESTAURANT_OWNER";

    public RestaurantResponseDto addRestaurant(RestaurantRequestDto request, String username) {
        // check if loggedInUser is a RESTAURANT_OWNER, fetch ownerInfo from auth-service

//        String ownerRole = webClientBuilder.build().get()
//                .uri("http://auth-service/api/v1/user/role",
//                        UriBuilder::build)
//                .retrieve()
//                .bodyToMono(String.class)
//                .block();
//        if(ownerRole == null) {
//            return "Error: Owner does not exist. Can't add this restaurant.";
//        }
//        else if(!ownerRole.equals(ROLE)) {
//            return "Error: Given owner is not a restaurant owner. Can't add this restaurant";
//        }

        Restaurant restaurant = Restaurant.builder()
                .name(request.getName())
                .description(request.getDescription())
                .address(request.getAddress())
                .contactInfo(request.getContactInfo())
                .rating(0.0)
                .owner(OwnerInfo.builder()
                                .username(username)
                                .build()
                )
                .build();

        Restaurant savedRestaurant = restaurantRepository.save(restaurant);
        log.info("Restaurant saved successfully: {}", savedRestaurant);
        restaurantKafkaService.publishRestaurantCreated(savedRestaurant);
        return convertToRestaurantResponseDto(savedRestaurant);
    }

    public RestaurantResponseDto updateRestaurant(
            String id,
            RestaurantRequestDto request) {

        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() ->
                        new RestaurantNotFound("Restaurant not found"));

        restaurant.setName(request.getName());
        restaurant.setDescription(request.getDescription());
        restaurant.setAddress(request.getAddress());
        restaurant.setContactInfo(request.getContactInfo());

        Restaurant updatedRestaurant =
                restaurantRepository.save(restaurant);

        restaurantKafkaService.publishRestaurantUpdated(updatedRestaurant);

        return convertToRestaurantResponseDto(updatedRestaurant);
    }

    public RestaurantResponseDto getRestaurant(String id) {
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new RestaurantNotFound("Restaurant not found."));

        List<FoodItemDto> foodItems = foodItemRepository.findByRestaurantId(id)
                .stream()
                .map(this::convertToFoodItemResponse)
                .toList();

        return convertToRestaurantResponseDto(restaurant);
    }

    public List<RestaurantResponseDto> getAllRestaurants() {
        List<Restaurant> restaurants = restaurantRepository.findAll();
        return restaurants.stream()
                .map(this::convertToRestaurantResponseDto)
                .toList();
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
               .restaurantId(foodItem.getRestaurantId())
               .build();
    }

    private RestaurantResponseDto convertToRestaurantResponseDto(Restaurant savedRestaurant) {
        List<FoodItemDto> foodItems = foodItemRepository
                        .findByRestaurantId(savedRestaurant.getId())
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
                .ownerUsername(savedRestaurant.getOwner().getUsername())
                .foodItems(foodItems.isEmpty() ? null : foodItems)
                .build();
    }

}

