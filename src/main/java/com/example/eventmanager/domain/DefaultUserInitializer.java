//package com.example.eventmanager.domain;
//
//import com.example.eventmanager.service.UserService;
//import jakarta.annotation.PostConstruct;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Component;
//
//@Component
//public class DefaultUserInitializer {
//    @Autowired
//    private UserService userService;
//
//    @Autowired
//    private PasswordEncoder passwordEncoder;
//
//    @PostConstruct
//    public void initUsers() {
//        createUserIfNotExists("admen", "admin", Role.ADMIN);
//        createUserIfNotExists("userr", "user", Role.USER);
//    }
//
//    private void createUserIfNotExists(String login,
//                                       String password,
//                                       Role role) {
//        if (userService.isUserExistByLogin(login)) {
//            return;
//        }
//        String hashPassword = passwordEncoder.encode(password);
//
//        User user = new User(
//                null,
//                login,
//                20,
//                role,
//                hashPassword
//        );
//
//        userService.createUser(user);
//    }
//}
