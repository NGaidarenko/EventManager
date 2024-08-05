package com.example.eventmanager.dto;

import com.example.eventmanager.domain.Role;

public record UserDto (
        Long id,
        String login,
        Integer age,
        Role role
) {
}
