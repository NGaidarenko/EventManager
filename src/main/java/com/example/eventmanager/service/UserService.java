package com.example.eventmanager.service;

import com.example.eventmanager.domain.User;
import com.example.eventmanager.entity.UserEntity;
import com.example.eventmanager.mapper.UserEntityMapper;
import com.example.eventmanager.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserEntityMapper userEntityMapper;


    public User getUserById(Long id) {
        log.info("Trying to get user by id: " + id);
        UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));

        return userEntityMapper.toDomain(userEntity);
    }

    public User createUser(User user) {
        log.info("Trying to create user: " + user);
        UserEntity userEntity = userEntityMapper.toEntity(user);
        userEntity = userRepository.save(userEntity);

        return userEntityMapper.toDomain(userEntity);
    }

    public Boolean isUserExistByLogin(String login) {
        log.info("Cheking if user exist with login: {}", login);
        return userRepository.existsByLogin(login);
    }

    public User getUserByLogin(String login) {
        log.info("Trying to get user by login: " + login);
        UserEntity userEntity = userRepository.findByLogin(login)
                .orElseThrow(() -> new EntityNotFoundException("User not found with login: " + login));

        return userEntityMapper.toDomain(userEntity);
    }

}
