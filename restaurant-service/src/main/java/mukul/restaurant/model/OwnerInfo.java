package mukul.restaurant.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class OwnerInfo {

    @Column(name = "owner_username")
    private String username;

    @Column(name = "owner_full_name")
    private String fullName;

    @Column(name = "owner_phone_number")
    private String phoneNumber;
}