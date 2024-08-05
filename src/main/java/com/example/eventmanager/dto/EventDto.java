package com.example.eventmanager.dto;

import com.example.eventmanager.domain.EventStatus;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;

public record EventDto(
        @NotNull(message = "Id is important")
        Long id,

        @NotBlank(message = "Name must be not blank")
        String name,

        @NotNull(message = "OwnerId must be not null")
        Long ownerId,

        @Positive(message = "Max places must be more zero")
        int maxPlaces,

        @PositiveOrZero(message = "Occupied places must be positive")
        int occupiedPlaces,

        @NotNull(message = "Date must be in future")
        LocalDateTime date,

        @PositiveOrZero(message = "Cost must be positive or zero")
        int cost,

        @Min(value = 30, message = "Duration must be at 30 minutes or more")
        int duration,

        @NotNull(message = "Location ID is important")
        Long locationId,

        @NotNull(message = "Status is important")
        EventStatus status
) {
}
