package com.mukul.authservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthRequest {

    private String email;
    private String username;
    private String password;

    public String getIdentity() {
        return email != null && !email.trim().isEmpty() ? email : username;
    }
}
