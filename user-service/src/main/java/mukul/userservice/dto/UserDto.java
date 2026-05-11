package mukul.userservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import mukul.userservice.model.Address;
import mukul.userservice.model.UserRole;

@Builder
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDto {
    private String id;
    private String fullName;
    private String email;
    private Long phoneNumber;
    private Address address;
    private UserRole userRole;
}
