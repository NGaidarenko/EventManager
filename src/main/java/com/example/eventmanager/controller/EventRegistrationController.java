package com.example.eventmanager.controller;

import com.example.eventmanager.dto.EventDto;
import com.example.eventmanager.mapper.EventDtoMapper;
import com.example.eventmanager.service.EventRegistrationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/events/registrations")
public class EventRegistrationController {

    private static final Logger log = LoggerFactory.getLogger(EventRegistrationController.class);


    @Autowired
    private EventRegistrationService registrationService;

    @Autowired
    private EventDtoMapper mapper;

    @PostMapping("/{eventId}")
    public ResponseEntity<Void> registrationOnEvent(@PathVariable Long eventId) {
        log.info("Trying to register on Event with id: {}", eventId);
        registrationService.registrationOnEvent(eventId);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("/cancel/{eventId}")
    public ResponseEntity<Void> cancelOnEvent(@PathVariable Long eventId) {
        log.info("Trying to canceled to event with id: {}", eventId);
        registrationService.cancelRegistrationOnEvent(eventId);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/my")
    public ResponseEntity<List<EventDto>> getUserRegisterEvents() {
        log.info("Trying to get all user event");
        List<EventDto> eventDtoList = registrationService.getUserRegisterEvents().stream()
                .map(mapper::toDto)
                .toList();
        return new ResponseEntity<>(eventDtoList, HttpStatus.OK);
    }
}
