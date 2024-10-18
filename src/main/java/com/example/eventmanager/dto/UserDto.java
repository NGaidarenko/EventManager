package com.example.eventmanager.dto;

import com.example.eventmanager.domain.Role;
import jakarta.validation.constraints.Email;

public record UserDto (
        Long id,
        @Email
        String login,
        Integer age,
        Role role
) {
}
