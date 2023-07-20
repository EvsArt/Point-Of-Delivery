package com.pointofdelivery.authorizationserver.model;

import lombok.Getter;

import java.util.Set;

public enum RolesEnum {
    ROLE_USER("user"),
    ROLE_ADMIN("admin"),
    ROLE_MODERATOR("moderator");

    @Getter
    private String name;
    RolesEnum(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static Set<RolesEnum> getAllRoles(){
        return Set.of(ROLE_USER, ROLE_ADMIN, ROLE_MODERATOR);
    }

}
