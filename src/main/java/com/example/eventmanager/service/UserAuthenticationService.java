package com.example.eventmanager.service;

import com.example.eventmanager.domain.User;
import com.example.eventmanager.dto.SignInRequest;
import com.example.eventmanager.jwt.JwtTokenManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserAuthenticationService {
    @Autowired
    private JwtTokenManager jwtTokenManager;

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public String authenticateUser(SignInRequest signInRequest) {
        if (!userService.isUserExistByLogin(signInRequest.login())) {
            throw new BadCredentialsException("Invalid username");
        }

        User user = userService.getUserByLogin(signInRequest.login());
        if (!passwordEncoder.matches(signInRequest.password(), user.password())) {
            throw new BadCredentialsException("Wrong password");
        }

        return jwtTokenManager.generateToken(user);
    }
}
