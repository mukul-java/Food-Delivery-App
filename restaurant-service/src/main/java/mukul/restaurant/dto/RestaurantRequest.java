package mukul.restaurant.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import mukul.restaurant.model.Address;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RestaurantRequest {
    private String name;
    private String description;
    private Address address;
    private List<Long> contactInfo;
    private Double rating;
}
