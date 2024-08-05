package com.example.eventmanager.dto;

import jakarta.validation.constraints.NotBlank;

public record SignInRequest (
        @NotBlank
        String login,

        @NotBlank
        String password
) {
}
