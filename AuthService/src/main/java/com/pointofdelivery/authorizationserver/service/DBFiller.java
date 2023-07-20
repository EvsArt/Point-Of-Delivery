package com.pointofdelivery.authorizationserver.service;

import com.pointofdelivery.authorizationserver.model.Role;
import com.pointofdelivery.authorizationserver.model.RolesEnum;
import com.pointofdelivery.authorizationserver.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DBFiller implements CommandLineRunner {

    private final RoleRepository roleRepository;

    public DBFiller(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        for (RolesEnum role: RolesEnum.getAllRoles()) {
            if(!roleRepository.existsByName(role))
                roleRepository.save(new Role(role));
        }
    }
}
