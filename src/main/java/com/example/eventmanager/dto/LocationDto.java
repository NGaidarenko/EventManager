package com.example.eventmanager.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record LocationDto(
    Long id,

    @NotBlank
    String name,

    @NotBlank
    String address,

    @NotNull
    Long capacity,

    String description
) {
}
