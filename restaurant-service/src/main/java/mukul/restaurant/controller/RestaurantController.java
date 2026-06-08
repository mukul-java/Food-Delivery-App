package mukul.restaurant.controller;

import lombok.RequiredArgsConstructor;
import mukul.restaurant.dto.ApiResponse;
import mukul.restaurant.dto.RestaurantRequestDto;
import mukul.restaurant.dto.RestaurantResponseDto;
import mukul.restaurant.service.RestaurantService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@PreAuthorize("hasAuthority('RESTAURANT_OWNER')")
@RestController
@RequestMapping("api/v1/restaurant")
@RequiredArgsConstructor
public class RestaurantController {
    private final RestaurantService restaurantService;
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ApiResponse<RestaurantResponseDto>> addRestaurant(@RequestBody RestaurantRequestDto request,
        @RequestHeader("loggedInUser") String username) {
//        return restaurantService.addRestaurant(request, username);
        RestaurantResponseDto response = restaurantService.addRestaurant(request, username);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(
                        ApiResponse.<RestaurantResponseDto>builder()
                                .success(true)
                                .message("Restaurant created successfully")
                                .data(response)
                                .build()
                );
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ApiResponse<List<RestaurantResponseDto>>> getAllRestaurants() {
        List<RestaurantResponseDto> response =  restaurantService.getAllRestaurants();

         return ResponseEntity.ok(
                ApiResponse.<List<RestaurantResponseDto>>builder()
                        .success(true)
                        .message("Login successful")
                        .data(response)
                        .build()
         );

    }

//    @PreAuthorize("hasAuthority('RESTAURANT_OWNER')")
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ApiResponse<RestaurantResponseDto>> getRestaurant(@PathVariable("id") String id) {
        RestaurantResponseDto response = restaurantService.getRestaurant(id);

        return ResponseEntity.ok(
                ApiResponse.<RestaurantResponseDto>builder()
                        .success(true)
                        .message("Restaurant fetched successfully")
                        .data(response)
                        .build()
        );
    }

    @GetMapping("/debug")
    public String debug(Authentication auth) {
        System.out.println(auth);
        System.out.println(auth.getAuthorities());
        return "OK";
    }
}
