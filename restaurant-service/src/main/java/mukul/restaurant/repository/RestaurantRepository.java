package mukul.restaurant.repository;

import mukul.restaurant.model.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantRepository extends JpaRepository<Restaurant, String> {
}
