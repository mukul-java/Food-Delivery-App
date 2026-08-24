package mukul.restaurant.config;

import java.util.*;

public enum Permission {
    RESTAURANT_READ,
    RESTAURANT_CREATE,
    RESTAURANT_UPDATE,
    FOODITEM_READ,
    FOODITEM_CREATE,
    FOODITEM_UPDATE,
    FOODITEM_UPDATE_QUANTITY,
    FOODITEM_DELETE;

    private static final Map<String, Set<Permission>> ROLE_PERMISSIONS = new HashMap<>();

    static {
        Set<Permission> customerPermissions = EnumSet.of(
                RESTAURANT_READ,
                FOODITEM_READ
        );

        Set<Permission> ownerPermissions = EnumSet.allOf(Permission.class);

        Set<Permission> deliveryPermissions = EnumSet.of(
                RESTAURANT_READ,
                FOODITEM_READ
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
