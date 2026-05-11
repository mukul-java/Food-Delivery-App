package mukul.restaurantservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import mukul.restaurantservice.model.Address;
import mukul.restaurantservice.model.FoodItem;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RestaurantDto {
    private String id;
    private String name;
    private String description;
    private Address address;
    private List<Long> contactInfo;
    private Double rating;
    private List<FoodItem> foodItems;
}
