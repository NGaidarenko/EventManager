package com.example.eventmanager.dto;

import com.example.eventmanager.domain.EventStatus;

import java.time.LocalDateTime;

public record EventSearchFilter (
    String name,
    Integer maxPlaces,
    Integer minPlaces,
    LocalDateTime dateStartAfter,
    LocalDateTime dateStartBefore,
    Integer minCost,
    Integer maxCost,
    Integer minDuration,
    Integer maxDuration,
    Long locationId,
    EventStatus status

){
}
