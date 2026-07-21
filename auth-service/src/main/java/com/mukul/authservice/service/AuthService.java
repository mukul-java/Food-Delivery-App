package com.mukul.authservice.service;

import com.mukul.authservice.ExceptionHandler.GlobalExceptionHandler;
import com.mukul.authservice.ExceptionHandler.UserAlreadyExistsException;
import com.mukul.authservice.ExceptionHandler.UserNotFoundException;
import com.mukul.authservice.dto.UserDto;
import com.mukul.authservice.model.DeliveryAgent;
import com.mukul.authservice.model.UserCredential;
import com.mukul.authservice.model.UserRole;
import com.mukul.authservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder  passwordEncoder;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    public UserDto saveUser(UserCredential credential) {
        userRepository.findByUsername(credential.getUsername())
                .ifPresent(user -> {
                    throw new UserAlreadyExistsException(
                            "User already exists with email: " + credential.getUsername()
                    );
                });

        // Encrypt the password before saving
        credential.setPassword( passwordEncoder.encode(credential.getPassword()));
//        credential.setPassword(credential.getPassword());
        UserCredential savedUser = userRepository.save(credential);

        return mapUserCredentialToUserDto(savedUser);
    }

    public String generateToken(UserDetails userDetails) {
//        UserCredential user = userRepository.findByUsername(username).orElseThrow();
        String role = userDetails.getAuthorities()
                .iterator()
                .next()
                .getAuthority();
        return jwtService.generateToken(userDetails.getUsername(), role);
    }

//    public void validateToken(String token) {
//        jwtService.validateToken(token, username);
//    }

    public UserDto getUser(long id) {
        UserCredential user = userRepository.findById(id).orElseThrow(()->
                new UserNotFoundException("User not found with id: " + id)
        );
        return mapUserCredentialToUserDto(user);
    }

    public UserDto updateUser(UserCredential user) {
        if(user.getId() == null){
            throw new RuntimeException("User with given id is null");
        }
        UserCredential existingUser = userRepository.findById(user.getId()).orElseThrow(()->
                new UserNotFoundException("User not found with id: " +user.getId())
        );

        UserDto response = updateUser(user, existingUser);
        return response;
    }

    private UserDto updateUser(UserCredential user, UserCredential userNew) {
        user.setFullName(userNew.getFullName());
        user.setUsername(userNew.getUsername());
        user.setPhoneNumber(userNew.getPhoneNumber());
        user.setAddress(userNew.getAddress());
        userRepository.save(user);
        return mapUserCredentialToUserDto(user);
    }

    private UserDto mapUserCredentialToUserDto(UserCredential user) {
        return UserDto.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .email(user.getUsername())
                .phoneNumber(user.getPhoneNumber())
                .address(user.getAddress())
                .userRole(user.getUserRole())
                .build();
    }

    public List<DeliveryAgent> getDeliveryAgents() {
        List<UserCredential> allUsers = userRepository.findAll();
        allUsers.stream().filter(userCredential -> userCredential.getUserRole().equals(UserRole.DELIVERY_AGENT));

        return allUsers.stream().map(user -> DeliveryAgent.builder()
                .name(user.getFullName())
                .address(user.getAddress())
                .phoneNumber(user.getPhoneNumber())
                .build()).toList();
    }

    public String getUserRole(String username) {
        Optional<UserCredential> userCredential = userRepository.findByUsername(username);
        return userCredential.map(credential -> credential.getUserRole().toString()).orElse(null);
    }
}
