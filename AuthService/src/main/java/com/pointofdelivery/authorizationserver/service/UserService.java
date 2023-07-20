package com.pointofdelivery.authorizationserver.service;

import com.pointofdelivery.authorizationserver.model.User;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    public User createDefaultUser(String username, String email, String password){
        return new User(
                username,
                email,
                password
        );
    }

}
