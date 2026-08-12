package mukul.deliveryservice.model;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetails {
    private String restaurantName;
    private Address restaurantAddress;
    private String customerName;
    private Address customerAddress;
    private Long customerPhoneNumber;
    private Long orderNumber;
    private Date createdAt;
    private Date updatedAt;
}