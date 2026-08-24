package mukul.restaurant.controller;

import lombok.RequiredArgsConstructor;
import mukul.restaurant.dto.ApiResponse;
import mukul.restaurant.dto.FoodItemDto;
import mukul.restaurant.service.FoodItemServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/fooditem")
@RequiredArgsConstructor
public class FoodItemController {
    @Autowired
    private FoodItemServiceImpl foodItemService;

    @PreAuthorize("hasAuthority('FOODITEM_CREATE')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ApiResponse<FoodItemDto>> addFoodItem(
            @RequestBody FoodItemDto foodItemDto,
            @RequestHeader(value = "loggedInUserId", required = false) String ownerId,
            @RequestHeader(value = "loggedInUser", required = false) String username) {
        String effectiveOwnerId = (ownerId != null && !ownerId.isBlank()) ? ownerId : username;
        FoodItemDto response = foodItemService.addFoodItem(foodItemDto, effectiveOwnerId);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(
                        ApiResponse.<FoodItemDto>builder()
                                .success(true)
                                .message("Food item created successfully")
                                .data(response)
                                .build()
                );
    }

    @PreAuthorize("hasAuthority('FOODITEM_READ')")
    @GetMapping("/{restaurantId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ApiResponse<List<FoodItemDto>>> getFoodItems(@PathVariable("restaurantId") String restaurantId) {
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

    // Get all food items irrespective of restaurant.
    @PreAuthorize("hasAuthority('FOODITEM_READ')")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ApiResponse<Page<FoodItemDto>>> getAllFoodItems(@RequestParam int page, int size) {
        Page<FoodItemDto> response = foodItemService.getAllFoodItems(page, size);
        return ResponseEntity.ok(
                ApiResponse.<Page<FoodItemDto>>builder()
                        .success(true)
                        .message("Food items fetched successfully")
                        .data(response)
                        .build()
        );
    }

    @PreAuthorize("hasAuthority('FOODITEM_UPDATE')")
    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ApiResponse<FoodItemDto>> updateFoodItem(
            @RequestBody FoodItemDto foodItemDto,
            @RequestHeader(value = "loggedInUserId", required = false) String ownerId,
            @RequestHeader(value = "loggedInUser", required = false) String username) {
        String effectiveOwnerId = (ownerId != null && !ownerId.isBlank()) ? ownerId : username;
        FoodItemDto response = foodItemService.updateFoodItem(foodItemDto, effectiveOwnerId);

        return ResponseEntity.ok(
                ApiResponse.<FoodItemDto>builder()
                        .success(true)
                        .message("Food item updated successfully")
                        .data(response)
                        .build()
        );
    }

    @PreAuthorize("hasAuthority('FOODITEM_UPDATE')")
    @PutMapping("/quantity")
    @ResponseStatus(HttpStatus.OK)
    public void updateFoodItemQuantity(@RequestParam List<String> foodItemIds, @RequestParam List<Integer> orderQuantities) {
        foodItemService.updateFoodItemQuantity(foodItemIds, orderQuantities);
    }

    @PreAuthorize("hasAuthority('FOODITEM_DELETE')")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ApiResponse<Void>> deleteFoodItem(
            @PathVariable("id") String id,
            @RequestHeader(value = "loggedInUserId", required = false) String ownerId,
            @RequestHeader(value = "loggedInUser", required = false) String username) {
        String effectiveOwnerId = (ownerId != null && !ownerId.isBlank()) ? ownerId : username;
        foodItemService.deleteFoodItem(id, effectiveOwnerId);

        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .success(true)
                        .message("Food item deleted successfully")
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
