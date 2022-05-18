package com.fillolej.mysns.models;

import lombok.Getter;

@Getter
public enum Permission {
    USERS_READ("users:read"),
    USERS_WRITE("users:write"),
    ADMINS_READ("admins:read"),
    ADMINS_WRITE("admins:write");

    private final String permission;

    Permission(String permission) {
        this.permission = permission;
    }
}
