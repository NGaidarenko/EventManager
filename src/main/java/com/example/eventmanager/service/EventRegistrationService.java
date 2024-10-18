package com.example.eventmanager.service;

import com.example.eventmanager.domain.Event;
import com.example.eventmanager.domain.EventStatus;
import com.example.eventmanager.domain.User;
import com.example.eventmanager.entity.EventEntity;
import com.example.eventmanager.entity.EventRegistrationEntity;
import com.example.eventmanager.entity.UserEntity;
import com.example.eventmanager.mapper.EventMapper;
import com.example.eventmanager.repository.EventRegistrationRepository;
import com.example.eventmanager.repository.EventRepository;
import com.example.eventmanager.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventRegistrationService {

    private static final Logger log = LoggerFactory.getLogger(EventRegistrationService.class);

    @Autowired
    private EventRegistrationRepository registrationRepository;

    @Autowired
    private UserAuthenticationService authenticationService;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private EventMapper mapper;

    @Autowired
    private EmailService emailService;

    public void registrationOnEvent(Long eventId) {
        log.info("Register to event with id: {}", eventId);
        User user = authenticationService.getCurrentAuthenticatedUser();

        EventEntity eventEntity = eventRepository.findById(eventId)
                .orElseThrow(() -> new EntityNotFoundException("Event with id: %s not founded".formatted(eventId)));

        if (eventEntity.getOwnerId().equals(user.id())) {
            log.info("Owner of event id: {} equals user id: {}", eventEntity.getOwnerId(), user.id());
            throw new IllegalArgumentException("Creator cannot register on his event");
        }

        if (!eventEntity.getStatus().equals(EventStatus.WAIT_START)) {
            log.info("Status of event: {}", eventEntity.getStatus());
            throw new IllegalArgumentException("Event already started, you can't to register");
        }

        if (registrationRepository.findByEventIdAndUserId(eventId, user.id()).isPresent()   ) {
            log.info("User with id: {} already register to event: {}", user.id(), eventId);
            throw new IllegalArgumentException("User already register at this event");
        }

        registrationRepository.save(new EventRegistrationEntity(
                null,
                user.id(),
                eventEntity)
        );

        emailService.sendMessage(List.of(user.login()),
                "Registration on event",
                "You are have been registration on event %s".formatted(eventEntity.getName()));
    }

    public void cancelRegistrationOnEvent(Long eventId) {
        log.info("Canceling from event with id: {}", eventId);
        User user = authenticationService.getCurrentAuthenticatedUser();
        EventEntity eventEntity = eventRepository.findById(eventId)
                .orElseThrow(() -> new EntityNotFoundException("Event with id: %s not founded".formatted(eventId)));

        if (!eventEntity.getStatus().equals(EventStatus.WAIT_START)) {
            throw new IllegalArgumentException("Event already started, you can't to register");
        }

        if (registrationRepository.findByEventIdAndUserId(eventId, user.id()).isEmpty()) {
            throw new IllegalArgumentException("User already register at this event");
        }
        log.info("Deleting from event with id: {}", eventId);
        registrationRepository.deleteByEventIdAndUserId(eventId, user.id());

        emailService.sendMessage(List.of(user.login()),
                "Unsubscribing from an event",
                "You have unsubscribed from the event %s".formatted(eventEntity.getName()));
    }

    public List<Event> getUserRegisterEvents() {
        User user = authenticationService.getCurrentAuthenticatedUser();

        List<EventEntity> entityList = registrationRepository.findRegisteredEvents(user.id());

        return entityList.stream()
                .map(mapper::toDomain)
                .toList();
    }

    public List<String> getUserOnEvent(Long id) {
        return registrationRepository.findAllByEventId(id);
    }
}
