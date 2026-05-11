package mukul.restaurantservice.controller;

import lombok.RequiredArgsConstructor;
import mukul.restaurantservice.dto.RestaurantDto;
import mukul.restaurantservice.dto.RestaurantRequest;
import mukul.restaurantservice.dto.RestaurantResponse;
import mukul.restaurantservice.service.RestaurantService;
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
