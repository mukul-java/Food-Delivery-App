package com.mukul.authservice.repository;

import com.mukul.authservice.model.UserCredential;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<UserCredential, String> {
    Optional<UserCredential> findByUsername(String username);
}
