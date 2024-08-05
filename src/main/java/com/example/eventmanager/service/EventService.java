package com.example.eventmanager.service;

import com.example.eventmanager.domain.*;
import com.example.eventmanager.dto.EventCreateRequestDto;
import com.example.eventmanager.dto.EventSearchFilter;
import com.example.eventmanager.dto.EventUpdateRequestDto;
import com.example.eventmanager.entity.EventEntity;
import com.example.eventmanager.mapper.EventMapper;
import com.example.eventmanager.repository.EventRepository;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EventService {

    private static final Logger log = LoggerFactory.getLogger(EventService.class);

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private LocationService locationService;

    @Autowired
    private UserAuthenticationService authenticationService;

    @Autowired
    private EventMapper eventMapper;

    public Event createEvent(EventCreateRequestDto createRequestDto) {
        log.info("Creating new event: {}", createRequestDto);
        Location location = locationService.getLocationById(createRequestDto.locationId());
        User user = authenticationService.getCurrentAuthenticatedUser();

        if (location.capacity() < createRequestDto.maxPlaces()) {
            throw new IllegalArgumentException("Capacity of location is less than create event: %s < %s"
                    .formatted(location.capacity(), createRequestDto.maxPlaces()));
        }

        EventEntity eventEntity = new EventEntity(
                null,
                createRequestDto.name(),
                user.id(),
                createRequestDto.maxPlaces(),
                List.of(),
                createRequestDto.date(),
                createRequestDto.cost(),
                createRequestDto.duration(),
                createRequestDto.locationId(),
                EventStatus.WAIT_START
        );

        eventEntity = eventRepository.save(eventEntity);
        log.info("Create new event: {}", eventEntity);

        return eventMapper.toDomain(eventEntity);
    }

    public Event getEventById(Long eventId) {
        log.info("Get event by id: {}", eventId);
        EventEntity eventEntity = eventRepository.findById(eventId)
                .orElseThrow(() -> new EntityNotFoundException("Cannot found event with id: %s".formatted(eventId)));

        return eventMapper.toDomain(eventEntity);
    }

    public void cancelEvent(Long eventId) {
        log.info("Canceling event with id: {}", eventId);
        checkCurrentUserCanModifyEvent(eventId);

        Event event = getEventById(eventId);
        if (event.status().equals(EventStatus.CANCELLED)) {
            log.info("Event with id:{} already CANCELLED", eventId);
            return;
        }

        if (event.status().equals(EventStatus.FINISHED) ||
                event.status().equals(EventStatus.STARTED)) {
            log.info("Event has status: {} and you cannot cancelled it", event.status());
            return;
        }

        eventRepository.changeEventStatus(eventId, EventStatus.CANCELLED);
    }


    public Event updateEvent(Long eventId, EventUpdateRequestDto updateRequest) {
        checkCurrentUserCanModifyEvent(eventId);
        EventEntity event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EntityNotFoundException("Event with id: %s not founded".formatted(eventId)));

        if (!event.getStatus().equals(EventStatus.WAIT_START)) {
            throw new IllegalArgumentException("Cannot modify event with status: %s".formatted(event.getStatus()));
        }

        if (updateRequest.maxPlaces() != null || updateRequest.locationId() != null) {
            Long locationId = Optional.ofNullable(updateRequest.locationId())
                    .orElse(event.getLocationId());
            int maxPlaces = Optional.ofNullable(updateRequest.maxPlaces())
                        .orElse(event.getMaxPlaces());

            Location location = locationService.getLocationById(locationId);

            if (location.capacity() < maxPlaces) {
                throw new IllegalArgumentException("Capacity of location less than max places: capacity=%s and maxPlaces=%s"
                        .formatted(location.capacity(), maxPlaces));
            }
        }

        if (updateRequest.maxPlaces() != null
                && event.getRegistrationList().size() > updateRequest.maxPlaces()) {
            throw new IllegalArgumentException(
                    "Registration count is more than maxPlaces: regCount=%s, maxPlaces=%s"
                            .formatted(event.getRegistrationList().size(), updateRequest.maxPlaces()));
        }

        Optional.ofNullable(updateRequest.name()).ifPresent(event::setName);
        Optional.ofNullable(updateRequest.maxPlaces()).ifPresent(event::setMaxPlaces);
        Optional.ofNullable(updateRequest.date()).ifPresent(event::setDate);
        Optional.ofNullable(updateRequest.cost()).ifPresent(event::setCost);
        Optional.ofNullable(updateRequest.duration()).ifPresent(event::setDuration);
        Optional.ofNullable(updateRequest.locationId()).ifPresent(event::setLocationId);

        eventRepository.save(event);

        return eventMapper.toDomain(event);
    }

    public List<Event> searchByFilter(EventSearchFilter searchFilter) {

        List<EventEntity> eventEntities = eventRepository.findEvents(
                searchFilter.name(),
                searchFilter.minPlaces(),
                searchFilter.maxPlaces(),
                searchFilter.dateStartAfter(),
                searchFilter.dateStartBefore(),
                searchFilter.minCost(),
                searchFilter.maxCost(),
                searchFilter.minDuration(),
                searchFilter.maxDuration(),
                searchFilter.locationId(),
                searchFilter.status()
        );

        return eventEntities.stream()
                .map(eventMapper::toDomain)
                .toList();
    }

    public List<Event> getUserEvents() {
        User user = authenticationService.getCurrentAuthenticatedUser();
        List<EventEntity> eventEntities = eventRepository.findAllByOwnerId(user.id());

        return eventEntities.stream()
                .map(eventMapper::toDomain)
                .toList();
    }

    private void checkCurrentUserCanModifyEvent(Long eventId) {
        var currentUser = authenticationService.getCurrentAuthenticatedUser();
        var event = getEventById(eventId);

        if (!event.ownerId().equals(currentUser.id())
                && !currentUser.role().equals(Role.ADMIN)) {
            throw new IllegalArgumentException("This user cannot modify this event");
        }
    }
}
