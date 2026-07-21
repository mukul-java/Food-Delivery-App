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
import static java.rmi.server.LogStream.log;

@Slf4j
@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    @Autowired
    private AuthService authService;

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ApiResponse<UserDto>> getUser(@PathVariable("id") long id, @RequestHeader("loggedInUser") String username) {
        log("Get user with username: "+ username);
        UserDto response = authService.getUser(id);
        return ResponseEntity.ok(
                ApiResponse.<UserDto>builder()
                        .success(true)
                        .message("user fetched successfully")
                        .data(response)
                        .build());

    }

    @PutMapping()
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ApiResponse<UserDto>> updateUser(@RequestBody UserCredential user, @RequestHeader("loggedInUser") String username) {
        // Only user can update his/her details check if the User in request is same as the loggedInUser or not
        if(!user.getUsername().equals(username)) {
            return ResponseEntity.badRequest().body(ApiResponse.<UserDto>builder()
                    .success(false)
                    .message("Username or username does not match")
                    .data(null)
                    .build());
        }
        UserDto response = authService.updateUser(user);
        return ResponseEntity.ok(
                ApiResponse.<UserDto>builder()
                        .success(true)
                        .message("user registered successfully")
                        .data(response)
                        .build());
    }

    @GetMapping("/delivery")
    public List<DeliveryAgent> getDeliveryAgents() {
        return authService.getDeliveryAgents();
    }

    @GetMapping("/role")
    public String getUserRole(String username) {
        return authService.getUserRole(username);
    }
}
