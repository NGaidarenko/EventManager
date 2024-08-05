package com.example.eventmanager.mapper;

import com.example.eventmanager.domain.Event;
import com.example.eventmanager.dto.EventDto;
import org.springframework.stereotype.Component;

@Component
public class EventDtoMapper {
    public EventDto toDto(Event event) {
        return new EventDto(
                event.id(),
                event.name(),
                event.ownerId(),
                event.maxPlaces(),
                event.registrationList().size(),
                event.date(),
                event.cost(),
                event.duration(),
                event.locationId(),
                event.status()
        );
    }
}
