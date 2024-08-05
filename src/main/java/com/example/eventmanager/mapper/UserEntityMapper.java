package com.example.eventmanager.mapper;

import com.example.eventmanager.domain.Role;
import com.example.eventmanager.domain.User;
import com.example.eventmanager.entity.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class UserEntityMapper {
    public UserEntity toEntity(User user) {
        return new UserEntity(
                user.id(),
                user.login(),
                user.age(),
                user.role().name(),
                user.password()
        );
    }

    public User toDomain(UserEntity userEntity) {
        return new User(
                userEntity.getId(),
                userEntity.getLogin(),
                userEntity.getAge(),
                Role.valueOf(userEntity.getRole()),
                userEntity.getPassword()
        );
    }
}
