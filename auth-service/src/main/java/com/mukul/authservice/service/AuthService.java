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
        if (credential.getEmail() != null && userRepository.existsByEmail(credential.getEmail())) {
            throw new UserAlreadyExistsException("User already exists with email: " + credential.getEmail());
        }
        if (credential.getUsername() != null && userRepository.existsByUsername(credential.getUsername())) {
            throw new UserAlreadyExistsException("User already exists with username: " + credential.getUsername());
        }

        // Encrypt the password before saving
        credential.setPassword(passwordEncoder.encode(credential.getPassword()));
        UserCredential savedUser = userRepository.save(credential);

        return mapUserCredentialToUserDto(savedUser);
    }

    public String generateToken(UserDetails userDetails) {
        String role = userDetails.getAuthorities()
                .iterator()
                .next()
                .getAuthority();
        String userId = "";
        if (userDetails instanceof com.mukul.authservice.config.CustomUserDetails customUserDetails) {
            userId = customUserDetails.getUserId();
        }
        return jwtService.generateToken(userDetails.getUsername(), role, userId);
    }

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
                new UserNotFoundException("User not found with id: " + user.getId())
        );

        return updateUser(existingUser, user);
    }

    private UserDto updateUser(UserCredential existingUser, UserCredential userNew) {
        existingUser.setFullName(userNew.getFullName());
        existingUser.setUsername(userNew.getUsername());
        existingUser.setEmail(userNew.getEmail());
        existingUser.setPhoneNumber(userNew.getPhoneNumber());
        existingUser.setAddress(userNew.getAddress());
        userRepository.save(existingUser);
        return mapUserCredentialToUserDto(existingUser);
    }

    private UserDto mapUserCredentialToUserDto(UserCredential user) {
        return UserDto.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .username(user.getUsername())
                .email(user.getEmail())
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

    public String getUserRole(String identifier) {
        Optional<UserCredential> userCredential = userRepository.findByEmail(identifier)
                .or(() -> userRepository.findByUsername(identifier));
        return userCredential.map(credential -> credential.getUserRole().toString()).orElse(null);
    }
}
