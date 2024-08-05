package com.example.eventmanager.domain;

public record EventRegistration (
    Long id,
    Long userId,
    Long eventId
) {
}
