package com.example.eventmanager.exceptions;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record ErrorMessageResponse(
        String message,
        String detailedMessage,
        @JsonFormat(shape = JsonFormat.Shape.STRING)
        LocalDateTime dateTime
) {

}
