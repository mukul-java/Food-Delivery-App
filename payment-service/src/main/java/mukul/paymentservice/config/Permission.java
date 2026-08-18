package mukul.paymentservice.config;

import java.util.*;

public enum Permission {
    PAYMENT_CREATE,
    PAYMENT_READ,
    PAYMENT_UPDATE;

    private static final Map<String, Set<Permission>> ROLE_PERMISSIONS = new HashMap<>();

    static {
        Set<Permission> customerPermissions = EnumSet.of(
                PAYMENT_CREATE,
                PAYMENT_READ,
                PAYMENT_UPDATE
        );

        Set<Permission> ownerPermissions = EnumSet.of(
                PAYMENT_READ
        );

        Set<Permission> adminPermissions = EnumSet.allOf(Permission.class);

        ROLE_PERMISSIONS.put("CUSTOMER", customerPermissions);
        ROLE_PERMISSIONS.put("RESTAURANT_OWNER", ownerPermissions);
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
