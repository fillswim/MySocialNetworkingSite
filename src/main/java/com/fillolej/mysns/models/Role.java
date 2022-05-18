package com.fillolej.mysns.models;

import lombok.Getter;

import java.util.Set;

@Getter
public enum Role {
    ROLE_USER(Set.of(
            Permission.USERS_READ, Permission.USERS_WRITE)),

    ROLE_ADMIN(Set.of(
            Permission.USERS_READ, Permission.USERS_WRITE,
            Permission.ADMINS_READ, Permission.ADMINS_WRITE));

    private final Set<Permission> permissions;

    Role(Set<Permission> permissions) {
        this.permissions = permissions;
    }
}
