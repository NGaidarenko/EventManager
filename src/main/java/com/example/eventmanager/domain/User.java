package com.example.eventmanager.domain;

public record User (
        Long id,
        String login,
        Integer age,
        Role role,
        String password
) {
}
