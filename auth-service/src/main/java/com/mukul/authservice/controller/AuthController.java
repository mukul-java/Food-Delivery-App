package com.mukul.authservice.controller;

import com.mukul.authservice.dto.ApiResponse;
import com.mukul.authservice.dto.AuthRequest;
import com.mukul.authservice.dto.AuthResponse;
import com.mukul.authservice.dto.UserDto;
import com.mukul.authservice.model.UserCredential;
import com.mukul.authservice.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ApiResponse<UserDto>> createUser(@RequestBody UserCredential user) {
        UserDto response = authService.saveUser(user);
        return ResponseEntity.ok(
                ApiResponse.<UserDto>builder()
                        .success(true)
                        .message("user registered successfully")
                        .data(response)
                        .build());
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> getToken(@RequestBody AuthRequest authRequest) {
        Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
        );
        UserDetails userDetails = (UserDetails) authenticate.getPrincipal();
        String token = authService.generateToken(userDetails);

        AuthResponse response = AuthResponse.builder()
                .token(token)
                .username(userDetails.getUsername())
                .role(userDetails.getAuthorities()
                        .stream()
                        .findFirst()
                        .map(a -> a.getAuthority())
                        .orElse(""))
                .build();

        return ResponseEntity.ok(
                ApiResponse.<AuthResponse>builder()
                        .success(true)
                        .message("Login successful")
                        .data(response)
                        .build()
        );
    }

//    @GetMapping("/validate")
//    public String validateToken(@RequestParam("token") String token) {
//        authService.validateToken(token);
//        return "Token is valid";
//    }

}
