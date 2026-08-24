package mukul.restaurant.service;

import mukul.restaurant.dto.FoodItemDto;
import org.springframework.data.domain.Page;

import java.util.List;

public interface FoodItemService {

    FoodItemDto addFoodItem(FoodItemDto foodItemDto, String username);

    List<FoodItemDto> getAllFoodItems(String restaurantId);

    Page<FoodItemDto> getAllFoodItems(int page, int size);

    FoodItemDto updateFoodItem(FoodItemDto foodItemDto, String username);

    void updateFoodItemQuantity(List<String> foodItemIds, List<Integer> orderQuantities);

    void deleteFoodItem(String foodItemId, String username);
}