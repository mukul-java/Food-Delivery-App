package com.mukul.authservice.controller;

import com.mukul.authservice.dto.ApiResponse;
import com.mukul.authservice.dto.UserDto;
import com.mukul.authservice.model.DeliveryAgent;
import com.mukul.authservice.model.UserCredential;
import com.mukul.authservice.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;

@Slf4j
@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    @Autowired
    private AuthService authService;

    @PreAuthorize("hasAuthority('USER_READ')")
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ApiResponse<UserDto>> getUser(@PathVariable("id") long id,
            @RequestHeader(value = "loggedInUser", required = false) String username) {
        log.info("Get user with username: " + username);
        UserDto response = authService.getUser(id);
        return ResponseEntity.ok(
                ApiResponse.<UserDto>builder()
                        .success(true)
                        .message("user fetched successfully")
                        .data(response)
                        .build());

    }

    @PreAuthorize("hasAuthority('USER_UPDATE')")
    @PutMapping()
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ApiResponse<UserDto>> updateUser(@RequestBody UserCredential user,
            @RequestHeader(value = "loggedInUser", required = false) String username) {
        // Only user can update his/her details check if the User in request is same as
        // the loggedInUser or not
        boolean isCallerMatched = username == null || (user.getEmail() != null && user.getEmail().equals(username))
                || (user.getUsername() != null && user.getUsername().equals(username));
        if (!isCallerMatched) {
            return ResponseEntity.badRequest().body(ApiResponse.<UserDto>builder()
                    .success(false)
                    .message("Logged in user identity does not match request user")
                    .data(null)
                    .build());
        }
        UserDto response = authService.updateUser(user);
        return ResponseEntity.ok(
                ApiResponse.<UserDto>builder()
                        .success(true)
                        .message("user updated successfully")
                        .data(response)
                        .build());
    }

    @PreAuthorize("hasAuthority('USER_READ_DELIVERY')")
    @GetMapping("/delivery")
    public List<DeliveryAgent> getDeliveryAgents() {
        return authService.getDeliveryAgents();
    }

    @GetMapping("/role")
    public String getUserRole(String username) {
        return authService.getUserRole(username);
    }
}
