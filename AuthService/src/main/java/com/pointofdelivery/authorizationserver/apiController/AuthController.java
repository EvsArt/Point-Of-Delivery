package com.pointofdelivery.authorizationserver.apiController;

import com.pointofdelivery.authorizationserver.config.jwt.JwtUtils;
import com.pointofdelivery.authorizationserver.connection.JwtResponse;
import com.pointofdelivery.authorizationserver.connection.LoginRequest;
import com.pointofdelivery.authorizationserver.connection.MessageResponse;
import com.pointofdelivery.authorizationserver.connection.SignUpRequest;
import com.pointofdelivery.authorizationserver.exceptions.EmailAlreadyExistException;
import com.pointofdelivery.authorizationserver.exceptions.UserAlreadyExistException;
import com.pointofdelivery.authorizationserver.model.Role;
import com.pointofdelivery.authorizationserver.model.User;
import com.pointofdelivery.authorizationserver.repository.RoleRepository;
import com.pointofdelivery.authorizationserver.repository.UserRepository;
import com.pointofdelivery.authorizationserver.service.RoleService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final RoleService roleService;
    private final UserDetailsManager userDetailsManager;

    public AuthController(AuthenticationManager authenticationManager,
                          UserRepository userRepository,
                          RoleRepository roleRepository,
                          PasswordEncoder passwordEncoder,
                          JwtUtils jwtUtils, RoleService roleService, UserDetailsManager userDetailsManager){
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
        this.roleService = roleService;
        this.userDetailsManager = userDetailsManager;
    }

    @PostMapping("/signin")
    public ResponseEntity<?> authUser(@RequestBody LoginRequest loginRequest){

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                ));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateToken(authentication);

        User user = (User) authentication.getPrincipal();
        Set<String> roles = user.getAuthorities().stream()
                .map((role) -> role.getAuthority())
                .collect(Collectors.toSet());

        return ResponseEntity.ok(new JwtResponse(jwt, user.getId(), user.getUsername(), user.getEmail(), roles));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody SignUpRequest signUpRequest){

        User user = new User(
                signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                signUpRequest.getPassword()
        );
        Set<String> stringRoles = signUpRequest.getRoles();
        Set<Role> roles = roleService.parseRoles(stringRoles);
        user.setRoles(roles);

        try {
            userDetailsManager.createUser(user);
        } catch (UserAlreadyExistException e){
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is exist"));
        } catch (EmailAlreadyExistException e){
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: This email is already in use"));
        }

        return ResponseEntity.ok(new MessageResponse("User created: " + user));
    }

}
