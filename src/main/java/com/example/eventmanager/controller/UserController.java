package com.example.eventmanager.controller;

import com.example.eventmanager.domain.User;
import com.example.eventmanager.dto.SignInRequest;
import com.example.eventmanager.dto.SignUpRequest;
import com.example.eventmanager.dto.UserDto;
import com.example.eventmanager.jwt.JwtTokenResponse;
import com.example.eventmanager.mapper.UserDtoMapper;
import com.example.eventmanager.service.UserAuthenticationService;
import com.example.eventmanager.service.UserRegistrationService;
import com.example.eventmanager.service.UserService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    @Autowired
    private UserService userService;

    @Autowired
    private UserDtoMapper userDtoMapper;

    @Autowired
    private UserRegistrationService registrationService;

    @Autowired
    private UserAuthenticationService userAuthenticationService;

    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long userId) {
        log.info("Trying to get user by id: " + userId);
        User user = userService.getUserById(userId);

        return new ResponseEntity<>(userDtoMapper.toDto(user), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<UserDto> registrationUser(@RequestBody @Valid SignUpRequest signUpRequest) {
        log.info("Registration new user: {}", signUpRequest);
        User user = registrationService.registerUser(signUpRequest);

        return new ResponseEntity<>(userDtoMapper.toDto(user), HttpStatus.CREATED);
    }

    @PostMapping("/auth")
    public ResponseEntity<JwtTokenResponse> authenticateUser(@RequestBody @Valid SignInRequest signInRequest) {
        log.info("Authenticating user: {}", signInRequest);
        String jwtToken = userAuthenticationService.authenticateUser(signInRequest);


        return new ResponseEntity<>(new JwtTokenResponse(jwtToken), HttpStatus.OK);
    }
}
