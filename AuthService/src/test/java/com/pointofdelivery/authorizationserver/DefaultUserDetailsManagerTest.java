//package com.pointofdelivery.authorizationserver;
//
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import java.util.Set;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//
//@RunWith(SpringRunner.class)
////@ContextConfiguration(classes = AuthorizationServerApplication.class)
//@SpringBootTest(classes = AuthorizationServerApplication.class)
//class DefaultUserDetailsManagerTest {
//
//    @Autowired
//    private UserRepository userRepository;
//    @Autowired
//    private UserService userService;
//    @Autowired
//    private DefaultUserDetailsManager defaultUserDetailsManager;
//
//    private final String USERNAME = "TestUser";
//    private final String PASSWORD = "1234";
//
////    User user = userService.createDefaultUser(USERNAME, PASSWORD);
//    User user = createDefaultUser(USERNAME, PASSWORD);
//
//    @BeforeEach
//    public void setup(){
//        userRepository.create(user);
//    }
//    @AfterEach
//    public void clear(){
//        userRepository.deleteByUsername(USERNAME);
//    }
//
//
//    @Test
//    void createUser() {
//
//        String newUsername = USERNAME+1;
//
////        User user = userService.createDefaultUser(newUsername, PASSWORD);
//        User user = createDefaultUser(newUsername, PASSWORD);
//        defaultUserDetailsManager.createUser(user);
//        User userFromDB = userRepository.findByUsername(newUsername).orElse(null);
//
//        assert userFromDB != null;
//        assertEquals(user.getUsername(), userFromDB.getUsername());
//
//    }
//
//    @Test
//    void updateUser() {
//
//        String updatedUsername = "UpdatedUser";
////        User updatedUser = userService.createDefaultUser(updatedUsername, PASSWORD);
//        User updatedUser = createDefaultUser(updatedUsername, PASSWORD);
//        defaultUserDetailsManager.updateUser(updatedUser);
//
//        User userFromDB = userRepository.findByUsername(updatedUsername).orElse(null);
//
//        userRepository.deleteByUsername(updatedUsername);
//
//        assert userFromDB != null;
//        assertEquals(userFromDB.getUsername(), updatedUsername);
//
//    }
//
//    @Test
//    void deleteUser() {
//
//        defaultUserDetailsManager.deleteUser(USERNAME);
//        assertFalse(userRepository.existsByUsername(USERNAME));
//
//    }
//
//    @Test
//    void userExists() {
//        assertFalse(defaultUserDetailsManager.userExists(USERNAME+USERNAME));
//        assertTrue(defaultUserDetailsManager.userExists(USERNAME));
//    }
//
//    @Test
//    void loadUserByUsername() {
//        assertEquals(
//        defaultUserDetailsManager.loadUserByUsername(USERNAME).getUsername(), USERNAME
//        );
//    }
//
//    @Test
//    void updatePassword() {
//
//        String updatedPassword = "4321";
//        defaultUserDetailsManager.updatePassword(user, updatedPassword);
//
//        User userFromDB = userRepository.findByUsername(USERNAME).orElse(null);
//
//        assert userFromDB != null;
//        assertEquals(updatedPassword, userFromDB.getPassword());
//
//    }
//
//    public User createDefaultUser(String username, String password){
//        return new User(
//                null,
//                username,
//                password,
//                Set.of(new Role("USER")),
//                true,
//                true,
//                true,
//                true
//        );
//    }
//
//}