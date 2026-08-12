package mukul.order.model;

import jakarta.persistence.Embeddable;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class Address {

    private String address;

    private String city;

    private String state;

    private Integer zipcode;
}