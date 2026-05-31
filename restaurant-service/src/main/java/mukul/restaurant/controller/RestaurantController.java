package mukul.restaurant.controller;

import lombok.RequiredArgsConstructor;
import mukul.restaurant.dto.RestaurantDto;
import mukul.restaurant.dto.RestaurantRequest;
import mukul.restaurant.dto.RestaurantResponse;
import mukul.restaurant.service.RestaurantService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

import static org.springframework.security.authorization.AuthorityAuthorizationManager.hasAuthority;

@PreAuthorize("hasRole('RESTAURANT_OWNER')")
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

    @PreAuthorize("hasAuthority('RESTAURANT_OWNER')")
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public RestaurantResponse getRestaurant(@PathVariable("id") String id) {
        return restaurantService.getRestaurant(id);
    }

    @GetMapping("/debug")
    public String debug(Authentication auth) {
        System.out.println(auth);
        System.out.println(auth.getAuthorities());
        return "OK";
    }
}
