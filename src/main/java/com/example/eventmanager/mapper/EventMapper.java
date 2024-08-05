package com.example.eventmanager.mapper;

import com.example.eventmanager.domain.Event;
import com.example.eventmanager.domain.EventRegistration;
import com.example.eventmanager.entity.EventEntity;
import org.springframework.stereotype.Component;

@Component
public class EventMapper {
    public Event toDomain(EventEntity eventEntity) {
        return new Event(
                eventEntity.getId(),
                eventEntity.getName(),
                eventEntity.getOwnerId(),
                eventEntity.getMaxPlaces(),
                eventEntity.getRegistrationList().stream()
                        .map(x -> new EventRegistration(
                                x.getId(),
                                x.getUserId(),
                                eventEntity.getId())
                        )
                        .toList(),
                eventEntity.getDate(),
                eventEntity.getCost(),
                eventEntity.getDuration(),
                eventEntity.getLocationId(),
                eventEntity.getStatus()
        );
    }
}
