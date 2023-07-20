package com.pointofdelivery.authorizationserver.connection;

import lombok.Data;

@Data
public class LoginRequest {
    private String username;
    private String password;
}
