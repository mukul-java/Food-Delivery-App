package com.mukul.authservice.config;

import com.mukul.authservice.model.UserCredential;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class CustomUserDetails implements UserDetails {

    private UserCredential userCredential;

    public CustomUserDetails(UserCredential userCredential) {
        this.userCredential = userCredential;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        String roleName = userCredential.getUserRole() != null ? userCredential.getUserRole().name() : "";
        Set<Permission> permissions = Permission.getPermissionsForRole(roleName);
        List<GrantedAuthority> authorities = new java.util.ArrayList<>();
        if (!roleName.isBlank()) {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + roleName));
            authorities.add(new SimpleGrantedAuthority(roleName));
        }
        for (Permission perm : permissions) {
            authorities.add(new SimpleGrantedAuthority(perm.name()));
        }
        return authorities;
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

    public String getUserId() {
        return userCredential != null && userCredential.getId() != null ? String.valueOf(userCredential.getId()) : "";
    }

    public UserCredential getUserCredential() {
        return userCredential;
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
