package mukul.deliveryservice.model;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.Date;

// This detail will be sent by order-service to deliveryService
@AllArgsConstructor
@NoArgsConstructor
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
