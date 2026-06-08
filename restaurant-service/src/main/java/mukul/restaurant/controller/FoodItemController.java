package mukul.restaurant.controller;

import lombok.RequiredArgsConstructor;
import mukul.restaurant.dto.ApiResponse;
import mukul.restaurant.dto.FoodItemDto;
import mukul.restaurant.service.FoodItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/fooditem")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('RESTAURANT_OWNER')")
public class FoodItemController {
    @Autowired
    private FoodItemService foodItemService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ApiResponse<FoodItemDto>> addFoodItem(@RequestBody FoodItemDto foodItemDto,
                                                                @RequestHeader("loggedInUser") String username) {
        FoodItemDto response = foodItemService.addFoodItem(foodItemDto,username);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(
                        ApiResponse.<FoodItemDto>builder()
                                .success(true)
                                .message("Food item created successfully")
                                .data(response)
                                .build()
                );
    }

    @GetMapping("/{restaurantId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ApiResponse<List<FoodItemDto>>> getAllFoodItems(@PathVariable("restaurantId") String restaurantId) {
        List<FoodItemDto> response = foodItemService.getAllFoodItems(restaurantId);
        return ResponseEntity.ok(
                ApiResponse.<List<FoodItemDto>>builder()
                        .success(true)
                        .message(
                                "Food items fetched successfully")
                        .data(response)
                        .build()
        );
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ApiResponse<FoodItemDto>> updateFoodItem(@RequestBody FoodItemDto foodItemDto,
                                 @RequestHeader("loggedInUser") String username) {
        FoodItemDto response = foodItemService.updateFoodItem(foodItemDto, username);

        return ResponseEntity.ok(
                ApiResponse.<FoodItemDto>builder()
                        .success(true)
                        .message("Food item updated successfully")
                        .data(response)
                        .build()
        );
    }

    @PutMapping("/quantity")
    @ResponseStatus(HttpStatus.OK)
    public void updateFoodItemQuantity(@RequestParam List<String> foodItemIds, @RequestParam List<Integer> orderQuantities) {
        foodItemService.updateFoodItemQuantity(foodItemIds, orderQuantities);
    }

    @GetMapping("/debug")
    public String debug(Authentication auth) {
        System.out.println(auth);
        System.out.println(auth.getAuthorities());
        return "OK";
    }
}
