package com.example.eventmanager.mapper;

import com.example.eventmanager.domain.User;
import com.example.eventmanager.dto.UserDto;
import org.springframework.stereotype.Component;

@Component
public class UserDtoMapper {
    public UserDto toDto(User user) {
        return new UserDto (
                user.id(),
                user.login(),
                user.age(),
                user.role()
        );
    }
}
