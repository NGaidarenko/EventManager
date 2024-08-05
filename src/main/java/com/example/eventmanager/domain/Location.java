package com.example.eventmanager.domain;

public record LocationDomain (
    Long id,
    String name,
    String address,
    Long capacity,
    String description
) {
}
