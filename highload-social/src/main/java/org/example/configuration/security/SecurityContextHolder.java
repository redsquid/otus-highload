package org.example.configuration.security;

import java.util.UUID;

public class SecurityContextHolder {

    private static final ThreadLocal<UUID> accountId = new ThreadLocal<>();

    public static void setAccountId(UUID value) {
        accountId.set(value);
    }

    public static UUID getAccountId() {
        return accountId.get();
    }
}
