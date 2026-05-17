package mukul.restaurant.controller;

import lombok.RequiredArgsConstructor;
import mukul.restaurant.dto.RestaurantDto;
import mukul.restaurant.dto.RestaurantRequest;
import mukul.restaurant.dto.RestaurantResponse;
import mukul.restaurant.service.RestaurantService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("api/v1/restaurant")
@RequiredArgsConstructor
public class RestaurantController {
    private final RestaurantService restaurantService;
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String addRestaurant(@RequestBody RestaurantRequest request) {

//        @RequestHeader("loggedInUser") String username) {
//        return restaurantService.addRestaurant(request, username);
        return restaurantService.addRestaurant(request, "Mukul");

    }
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<RestaurantDto> getAllRestaurants() {
        return restaurantService.getAllRestaurants();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public RestaurantResponse getRestaurant(@PathVariable("id") String id) {
        return restaurantService.getRestaurant(id);
    }
}
