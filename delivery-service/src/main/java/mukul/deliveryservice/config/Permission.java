package mukul.deliveryservice.config;

import java.util.*;

public enum Permission {
    DELIVERY_MANAGE;

    private static final Map<String, Set<Permission>> ROLE_PERMISSIONS = new HashMap<>();

    static {
        Set<Permission> deliveryPermissions = EnumSet.of(
                DELIVERY_MANAGE
        );

        Set<Permission> adminPermissions = EnumSet.allOf(Permission.class);

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
