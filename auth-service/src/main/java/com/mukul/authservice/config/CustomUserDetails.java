package com.mukul.authservice.config;

import com.mukul.authservice.model.UserCredential;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class CustomUserDetails implements UserDetails {

    private UserCredential userCredential;

    public CustomUserDetails(UserCredential userCredential) {
        this.userCredential = userCredential;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(
                new SimpleGrantedAuthority(
                        userCredential.getUserRole().name()
                )
        );
    }

    @Override
    public String getPassword() {
        return userCredential.getPassword();
    }

    @Override
    public String getUsername() {
        return userCredential.getEmail() != null ? userCredential.getEmail() : userCredential.getUsername();
    }

    public String getEmail() {
        return userCredential.getEmail();
    }

    public String getActualUsername() {
        return userCredential.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
