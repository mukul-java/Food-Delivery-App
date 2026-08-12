package com.mukul.authservice.config;

import com.mukul.authservice.model.UserCredential;
import com.mukul.authservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserCredential> credential = userRepository.findByEmail(username)
                .or(() -> userRepository.findByUsername(username));
        return credential.map(CustomUserDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email/username: " + username));
    }
}
