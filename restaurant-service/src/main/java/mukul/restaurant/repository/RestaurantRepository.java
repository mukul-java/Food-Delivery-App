package mukul.restaurant.repository;

import mukul.restaurant.model.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RestaurantRepository extends JpaRepository<Restaurant, String> {
    List<Restaurant> findByOwnerId(String ownerId);
}
