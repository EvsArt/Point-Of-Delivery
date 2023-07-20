package com.pointofdelivery.authorizationserver.connection;

import lombok.Data;

import java.util.Set;

@Data
public class SignUpRequest {
    private String username;
    private String email;
    private Set<String> roles;
    private String password;
}
