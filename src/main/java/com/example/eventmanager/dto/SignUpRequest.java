package com.example.eventmanager.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record SignUpRequest (
    @NotBlank
    String login,

    @NotBlank
    String password,

    @Min(18)
    Integer age
) {
}
