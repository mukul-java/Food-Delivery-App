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

@RestController
@RequestMapping("api/v1/restaurant")
@RequiredArgsConstructor
public class RestaurantController {
    private final RestaurantService restaurantService;

    @PreAuthorize("hasAuthority('RESTAURANT_CREATE')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ApiResponse<RestaurantResponseDto>> addRestaurant(
            @RequestBody RestaurantRequestDto request,
            @RequestHeader(value = "loggedInUserId", required = false) String ownerId,
            @RequestHeader(value = "loggedInUser", required = false) String username) {
        String effectiveOwnerId = (ownerId != null && !ownerId.isBlank()) ? ownerId : username;
        RestaurantResponseDto response = restaurantService.addRestaurant(request, effectiveOwnerId);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(
                        ApiResponse.<RestaurantResponseDto>builder()
                                .success(true)
                                .message("Restaurant created successfully")
                                .data(response)
                                .build()
                );
    }

    @PreAuthorize("hasAuthority('RESTAURANT_UPDATE')")
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ApiResponse<RestaurantResponseDto>> updateRestaurant(
            @PathVariable String id,
            @RequestBody RestaurantRequestDto request,
            @RequestHeader(value = "loggedInUserId", required = false) String ownerId,
            @RequestHeader(value = "loggedInUser", required = false) String username) {

        String effectiveOwnerId = (ownerId != null && !ownerId.isBlank()) ? ownerId : username;
        RestaurantResponseDto response = restaurantService.updateRestaurant(id, request, effectiveOwnerId);

        return ResponseEntity.ok(
                ApiResponse.<RestaurantResponseDto>builder()
                        .success(true)
                        .message("Restaurant updated successfully")
                        .data(response)
                        .build()
        );
    }

    @PreAuthorize("hasAuthority('RESTAURANT_READ')")
    @GetMapping("/owner")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ApiResponse<List<RestaurantResponseDto>>> getOwnerRestaurants(
            @RequestHeader(value = "loggedInUserId", required = false) String ownerId,
            @RequestHeader(value = "loggedInUser", required = false) String username) {
        String effectiveOwnerId = (ownerId != null && !ownerId.isBlank()) ? ownerId : username;
        List<RestaurantResponseDto> response = restaurantService.getRestaurantsByOwnerId(effectiveOwnerId);

        return ResponseEntity.ok(
                ApiResponse.<List<RestaurantResponseDto>>builder()
                        .success(true)
                        .message("Owner restaurants fetched successfully")
                        .data(response)
                        .build()
        );
    }

    @PreAuthorize("hasAuthority('RESTAURANT_READ')")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ApiResponse<List<RestaurantResponseDto>>> getAllRestaurants() {
        List<RestaurantResponseDto> response =  restaurantService.getAllRestaurants();

         return ResponseEntity.ok(
                ApiResponse.<List<RestaurantResponseDto>>builder()
                        .success(true)
                        .message("Restaurants fetched successfully")
                        .data(response)
                        .build()
         );

    }

    @PreAuthorize("hasAuthority('RESTAURANT_READ')")
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
