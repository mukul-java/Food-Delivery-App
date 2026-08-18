package com.mukul.authservice.config;

import java.util.*;

public enum Permission {
    USER_READ,
    USER_UPDATE,
    USER_READ_DELIVERY;

    private static final Map<String, Set<Permission>> ROLE_PERMISSIONS = new HashMap<>();

    static {
        Set<Permission> customerPermissions = EnumSet.of(
                USER_READ,
                USER_UPDATE
        );

        Set<Permission> ownerPermissions = EnumSet.of(
                USER_READ,
                USER_UPDATE,
                USER_READ_DELIVERY
        );

        Set<Permission> deliveryPermissions = EnumSet.of(
                USER_READ,
                USER_UPDATE
        );

        Set<Permission> adminPermissions = EnumSet.allOf(Permission.class);

        ROLE_PERMISSIONS.put("CUSTOMER", customerPermissions);
        ROLE_PERMISSIONS.put("RESTAURANT_OWNER", ownerPermissions);
        ROLE_PERMISSIONS.put("DELIVERY_AGENT", deliveryPermissions);
        ROLE_PERMISSIONS.put("DELIVERY_PARTNER", deliveryPermissions);
        ROLE_PERMISSIONS.put("ADMIN", adminPermissions);
    }

    public static Set<Permission> getPermissionsForRole(String role) {
        if (role == null || role.isBlank()) {
            return Collections.emptySet();
        }
        String cleanRole = role.startsWith("ROLE_") ? role.substring(5) : role;
        return ROLE_PERMISSIONS.getOrDefault(cleanRole, Collections.emptySet());
    }
}
