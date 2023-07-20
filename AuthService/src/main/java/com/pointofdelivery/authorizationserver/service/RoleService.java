package com.pointofdelivery.authorizationserver.service;

import com.pointofdelivery.authorizationserver.exceptions.RoleNotFoundException;
import com.pointofdelivery.authorizationserver.model.Role;
import com.pointofdelivery.authorizationserver.model.RolesEnum;
import com.pointofdelivery.authorizationserver.repository.RoleRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Service
public class RoleService {

    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    private final Map<String, RolesEnum> roleNameToRoleMap = new HashMap<>();

    public Set<Role> parseRoles(Set<String> stringRoles){

        Set<Role> result = new HashSet<>();

        if(stringRoles == null){

            result.add(getUserRole());
        } else{
            stringRoles.forEach((role) -> {
                result.add(
                        roleRepository
                        .findByName(
                                roleNameToRolesEnum(role.toLowerCase())
                        )
                        .orElseThrow(() -> new RoleNotFoundException("Role " + role + " is not found"))
                );

            });
        }

        if(result.isEmpty()) result.add(getUserRole());

        return result;


    }

    public RolesEnum roleNameToRolesEnum(String roleName){
        if(roleNameToRoleMap.isEmpty()){
            fillRoleNameToRoleMap();    // Something like caching
        }
        return roleNameToRoleMap.get(roleName);
    }

    private void fillRoleNameToRoleMap(){
        Set<RolesEnum> rolesEnum = RolesEnum.getAllRoles();
        rolesEnum.forEach(
                (rEnum) -> roleNameToRoleMap.put(rEnum.getName(), rEnum)
        );
    }

    private Role getUserRole(){
        return roleRepository
                .findByName(RolesEnum.ROLE_USER)
                .orElseThrow(() -> new RuntimeException("Role USER is not found"));
    }

}
