package mukul.restaurant.service;

import mukul.restaurant.dto.RestaurantDto;
import mukul.restaurant.dto.RestaurantRequest;
import mukul.restaurant.dto.RestaurantResponse;
import mukul.restaurant.model.FoodItem;
import mukul.restaurant.model.OwnerInfo;
import mukul.restaurant.model.Restaurant;
import mukul.restaurant.repository.FoodItemRepository;
import mukul.restaurant.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RestaurantService {
    @Autowired
    private RestaurantRepository restaurantRepository;
    @Autowired
    private FoodItemRepository foodItemRepository;

    @Autowired
    private WebClient.Builder webClientBuilder;

    private final String ROLE = "RESTAURANT_OWNER";

    public String addRestaurant(RestaurantRequest request, String username) {
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
                .contactInfo(request.getContactInfo())
                .address(request.getAddress())
                .rating(request.getRating())
                .owner(OwnerInfo.builder()
                        .username(username)
                        .build())
                .build();
        restaurantRepository.save(restaurant);
        return "Restaurant with id: " + restaurant.getId() + " added successfully";
    }

    public RestaurantResponse getRestaurant(String id) {
        Optional<Restaurant> restaurant = restaurantRepository.findById(id);
        RestaurantResponse restaurantResponse = new RestaurantResponse();
        if(restaurant.isPresent()) {
            return RestaurantResponse.builder()
                    .restaurantDto(getRestaurantDto(restaurant.get()))
                    .responseCode(200)
                    .msg("Success")
                    .build();
        }
        return RestaurantResponse.builder()
                .responseCode(404)
                .msg("Restaurant not found with this id")
                .build();
    }

    public List<RestaurantDto> getAllRestaurants() {
        List<Restaurant> restaurants = restaurantRepository.findAll();
        return restaurants.stream()
                .map(this::getRestaurantDto).toList();
    }

    private RestaurantDto getRestaurantDto(Restaurant restaurant) {
        RestaurantDto dto = RestaurantDto.builder()
                .id(restaurant.getId())
                .name(restaurant.getName())
                .description(restaurant.getDescription())
                .address(restaurant.getAddress())
                .contactInfo(restaurant.getContactInfo())
                .rating(restaurant.getRating())
                .build();
        List<FoodItem> foodItems = foodItemRepository
                .findByRestaurantId(restaurant.getId());
        dto.setFoodItems(foodItems);
        return dto;
    }
}
