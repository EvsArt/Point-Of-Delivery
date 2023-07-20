package com.pointofdelivery.authorizationserver.service;

import com.pointofdelivery.authorizationserver.exceptions.AccessDeniedException;
import com.pointofdelivery.authorizationserver.exceptions.EmailAlreadyExistException;
import com.pointofdelivery.authorizationserver.exceptions.UserAlreadyExistException;
import com.pointofdelivery.authorizationserver.exceptions.UserNotFoundException;
import com.pointofdelivery.authorizationserver.model.User;
import com.pointofdelivery.authorizationserver.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsPasswordService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Slf4j
public class DefaultUserDetailsManager implements UserDetailsManager, UserDetailsPasswordService {

    private final SecurityContextHolderStrategy securityContextHolderStrategy = SecurityContextHolder
            .getContextHolderStrategy();

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public DefaultUserDetailsManager(UserRepository userRepository,
                                     PasswordEncoder passwordEncoder){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void createUser(UserDetails userDetails) throws UserAlreadyExistException, EmailAlreadyExistException {
        User user = (User) userDetails;
        if(userRepository.existsByUsername(user.getUsername())) {
            throw new UserAlreadyExistException("User is already exist");
        }
        if(userRepository.existsByEmail(user.getEmail())) {
            throw new EmailAlreadyExistException("This email is already exist");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user = userRepository.save(user);

        log.info("User {} was created", user);
    }

    @Override
    public void updateUser(UserDetails user) {
        Optional<User> userWithId = userRepository.findByUsername(user.getUsername());
        User updatedUser;
        if(userWithId.isPresent())
                updatedUser = userRepository.save(userWithId.get());
        else
                throw new UserNotFoundException("User " + user.getUsername() + " was not found");

        Authentication currentUser = this.securityContextHolderStrategy.getContext().getAuthentication();
        String initiatorUsername = currentUser.getName();

        log.info("User {} was updated to {} by {}", userWithId.get(), updatedUser, initiatorUsername);
    }

    @Override
    public void deleteUser(String username) {
        if(!userRepository.existsByUsername(username)){
            throw new UserNotFoundException("User " + username + " was not found");
        }

        userRepository.deleteByUsername(username);

        Authentication currentUser = this.securityContextHolderStrategy.getContext().getAuthentication();
        String initiatorUsername = currentUser.getName();

        log.info("User {} was deleted by {}", username, initiatorUsername);
    }

    @Override
    public void changePassword(String oldPassword, String newPassword) {    // Working with non-encrypted passwords
        Authentication currentUser = this.securityContextHolderStrategy.getContext().getAuthentication();
        String username = currentUser.getName();

        Optional<User> user = userRepository.findByUsername(username);
        if(user.isPresent()) {

            if (!passwordIsCorrect(user.get(), oldPassword)) {
                log.warn("Was sent wrong password for user {}", user.get().getUsername());
                throw new AccessDeniedException("Password is wrong!");
            }

            updatePassword(user.get(), newPassword);

        } else throw new AccessDeniedException("You are not logged in");


    }

    @Override
    public boolean userExists(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> it = userRepository.findByUsername(username);
        if(it.isPresent()){
            return it.get();
        }else {
            throw new UsernameNotFoundException("Username not exists");
        }
    }

    @Override
    public UserDetails updatePassword(UserDetails userDetails, String newPassword) {   // Working with non-encrypted passwords
        Optional<User> user = userRepository.findByUsername(userDetails.getUsername());
        if(user.isPresent()) {
            user.get().setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(user.get());
        } else
            throw new UserNotFoundException("User with this username was not found");

        log.info("Password for {} was changed", user);

        return user.get();
    }

    private boolean passwordIsCorrect(UserDetails userDetails, String password){
        Optional<User> user = userRepository.findByUsername(userDetails.getUsername());
        if(user.isEmpty()) {
            throw new UserNotFoundException("User " + userDetails.getUsername() + " was not found!");
        }
        return passwordEncoder.matches(password, user.get().getPassword());

    }
}
