package com.example.eventmanager.service;

import com.example.eventmanager.domain.Role;
import com.example.eventmanager.domain.User;
import com.example.eventmanager.dto.SignUpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserRegistrationService {

    private static final Logger log = LoggerFactory.getLogger(UserRegistrationService.class);
    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User registerUser(SignUpRequest signUpRequest) {
        log.info("Registering user with details: {}", signUpRequest);

        if (userService.isUserExistByLogin(signUpRequest.login())) {
            throw new IllegalArgumentException("User with this login: %s already exists".formatted(signUpRequest.login()));
        }
        String hashPassword = passwordEncoder.encode(signUpRequest.password());

        User user = new User(
                null,
                signUpRequest.login(),
                signUpRequest.age(),
                Role.USER,
                hashPassword
        );

        return userService.createUser(user);
    }
}
