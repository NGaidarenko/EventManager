package com.example.eventmanager.domain;

public record Location(
    Long id,
    String name,
    String address,
    Long capacity,
    String description
) {
}
