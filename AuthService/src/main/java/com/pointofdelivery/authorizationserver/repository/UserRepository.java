package com.pointofdelivery.authorizationserver.repository;

import com.pointofdelivery.authorizationserver.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findByUsername(String username);
    void deleteByUsername(String username);
    boolean existsByUsername(String username);

    boolean existsByEmail(String email);
}
