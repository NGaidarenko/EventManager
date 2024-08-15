package com.example.eventmanager.controller;

import com.example.eventmanager.domain.Event;
import com.example.eventmanager.dto.EventCreateRequestDto;
import com.example.eventmanager.dto.EventDto;
import com.example.eventmanager.dto.EventSearchFilter;
import com.example.eventmanager.dto.EventUpdateRequestDto;
import com.example.eventmanager.mapper.EventDtoMapper;
import com.example.eventmanager.service.EventService;
import com.example.eventmanager.service.NotificationService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/events")
public class EventController {
    private static final Logger log = LoggerFactory.getLogger(EventController.class);

    @Autowired
    private EventService eventService;

    @Autowired
    private EventDtoMapper dtoMapper;

    @PostMapping
    public ResponseEntity<EventDto> createEvent(@RequestBody @Valid EventCreateRequestDto createRequestDto) {
        log.info("Creating new event: {}", createRequestDto);
        Event event = eventService.createEvent(createRequestDto);

        return new ResponseEntity<>(dtoMapper.toDto(event), HttpStatus.CREATED);
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<EventDto> getEventById(@PathVariable Long eventId) {
        log.info("Getting event by id: {}", eventId);
        Event event = eventService.getEventById(eventId);

        return new ResponseEntity<>(dtoMapper.toDto(event), HttpStatus.OK);
    }

    @DeleteMapping("/{eventId}")
    public ResponseEntity<Void> cancelEvent(@PathVariable Long eventId) {
        log.info("Canceling event with id: {}", eventId);
        eventService.cancelEvent(eventId);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{eventId}")
    public ResponseEntity<EventDto> updateEvent(@PathVariable Long eventId,
                                                @RequestBody EventUpdateRequestDto updateRequestDto) {
        log.info("Updating event with id: {} and eventDto: {}", eventId, updateRequestDto);

        Event updatedEvent = eventService.updateEvent(eventId, updateRequestDto);

        return new ResponseEntity<>(dtoMapper.toDto(updatedEvent), HttpStatus.OK);
    }

    @PostMapping("/search")
    public ResponseEntity<List<EventDto>> searchEvent(@RequestBody EventSearchFilter eventSearchFilter) {
        log.info("Search event with filter: {}", eventSearchFilter);

        List<Event> event = eventService.searchByFilter(eventSearchFilter);

        return new ResponseEntity<>(event.stream()
                .map(dtoMapper::toDto)
                .toList(),
                HttpStatus.OK);
    }

    @GetMapping("/my")
    public ResponseEntity<List<EventDto>> getUserEvent() {
        log.info("Get user events");

        List<Event> events = eventService.getUserEvents();
        return new ResponseEntity<>(events.stream()
                .map(dtoMapper::toDto)
                .toList(),
                HttpStatus.OK);
    }
}
