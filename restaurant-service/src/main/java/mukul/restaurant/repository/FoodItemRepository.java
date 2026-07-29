package mukul.restaurant.repository;

import mukul.restaurant.model.FoodItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FoodItemRepository extends JpaRepository<FoodItem, String> {
    List<FoodItem> findByRestaurant_Id(String restaurantId);
}
