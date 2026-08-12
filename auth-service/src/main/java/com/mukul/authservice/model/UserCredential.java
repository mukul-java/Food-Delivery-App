package com.mukul.authservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
@Table(name = "users")
public class UserCredential {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String fullName;
    private String username;
    @Column(unique = true)
    private String email;
    private String password;
    private Long phoneNumber;
    private Address address;
    @Enumerated(EnumType.STRING)
    private UserRole userRole;
}
