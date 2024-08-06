package com.example.eventmanager.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;

public record EventCreateRequestDto(
        @NotBlank(message = "Name must be not blank")
        String name,

        @NotNull(message = "Max places must be not null")
        int maxPlaces,

        @JsonFormat(shape = JsonFormat.Shape.STRING)
        @Future(message = "Date must be in future")
        LocalDateTime date,

        @PositiveOrZero(message = "Cost must be more zero")
        int cost,

        @Min(value = 30, message = "Duration must be 30 min or more")
        int duration,

        @NotNull(message = "Location Id is important")
        Long locationId

) {
}
