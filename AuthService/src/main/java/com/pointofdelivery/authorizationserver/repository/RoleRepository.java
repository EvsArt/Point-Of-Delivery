package com.pointofdelivery.authorizationserver.repository;

import com.pointofdelivery.authorizationserver.model.Role;
import com.pointofdelivery.authorizationserver.model.RolesEnum;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface RoleRepository extends CrudRepository<Role, Long> {
    Optional<Role> findByName(RolesEnum name);

    boolean existsByName(RolesEnum role);
}
