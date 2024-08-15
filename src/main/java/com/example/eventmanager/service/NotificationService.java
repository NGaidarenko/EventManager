package com.example.eventmanager.service;

import com.example.eventmanager.domain.Event;
import com.example.eventmanager.domain.EventFieldChange;
import com.example.eventmanager.domain.EventStatus;
import com.example.eventmanager.dto.EventChangeKafkaMessage;
import com.example.eventmanager.dto.EventUpdateRequestDto;
import com.example.eventmanager.entity.EventEntity;
import com.example.eventmanager.kafka.KafkaProducer;
import com.example.eventmanager.repository.EventRepository;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class NotificationService {
    private static final Logger log = LoggerFactory.getLogger(NotificationService.class);

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private KafkaProducer kafkaProducer;

    @Autowired
    private UserAuthenticationService authenticationService;

    public void changeEventStatus(Long eventId, EventStatus status) {
        log.info("Changing status: {}, for event with id: {}", status, eventId);
        EventEntity event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EntityNotFoundException("Event with id: %s not founded".formatted(eventId)));

        var changedEventMessage = new EventChangeKafkaMessage();
        changedEventMessage.setEventId(eventId);
        changedEventMessage.setOwnerId(event.getOwnerId());
        changedEventMessage.setChangeById(authenticationService.getCurrentAuthenticatedUser().id());

        changedEventMessage.setStatus(new EventFieldChange<>(event.getStatus(), status));

        kafkaProducer.sendMessage(changedEventMessage);
    }

    public void changeEventFields(EventEntity event, EventUpdateRequestDto updateRequestDto) {
        log.info("Change event param: {} to {}", event, updateRequestDto);

        var changedEventMessage = new EventChangeKafkaMessage();
        changedEventMessage.setEventId(event.getId());
        changedEventMessage.setOwnerId(event.getOwnerId());
        changedEventMessage.setChangeById(authenticationService.getCurrentAuthenticatedUser().id());


        Optional.ofNullable(updateRequestDto.name())
                .filter(elem -> !elem.equals(event.getName()))
                .ifPresent(elem -> changedEventMessage
                        .setName(new EventFieldChange<>(event.getName(), elem)));

        Optional.ofNullable(updateRequestDto.maxPlaces())
                .filter(elem -> !elem.equals(event.getMaxPlaces()))
                .ifPresent(elem -> changedEventMessage
                        .setMaxPlaces(new EventFieldChange<>(event.getMaxPlaces(), elem)));

        Optional.ofNullable(updateRequestDto.date())
                .filter(elem -> !elem.equals(event.getDate()))
                .ifPresent(elem -> changedEventMessage
                        .setDate(new EventFieldChange<>(event.getDate(), elem)));

        Optional.ofNullable(updateRequestDto.cost())
                .filter(elem -> !elem.equals(event.getCost()))
                .ifPresent(elem -> changedEventMessage
                        .setCost(new EventFieldChange<>(event.getCost(), elem)));

        Optional.ofNullable(updateRequestDto.duration())
                .filter(elem -> !elem.equals(event.getDuration()))
                .ifPresent(elem -> changedEventMessage
                        .setDuration(new EventFieldChange<>(event.getDuration(), elem)));

        Optional.ofNullable(updateRequestDto.locationId())
                .filter(elem -> !elem.equals(event.getLocationId()))
                .ifPresent(elem -> changedEventMessage
                        .setLocationId(new EventFieldChange<>(event.getLocationId(), elem)));

        log.info("New changes after send message: {}",changedEventMessage);
        kafkaProducer.sendMessage(changedEventMessage);
    }
}
