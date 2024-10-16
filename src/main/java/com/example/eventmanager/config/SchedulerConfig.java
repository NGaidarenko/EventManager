package com.example.eventmanager.config;

import com.example.eventmanager.domain.EventStatus;
import com.example.eventmanager.repository.EventRepository;
import com.example.eventmanager.service.EmailService;
import com.example.eventmanager.service.EventRegistrationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@EnableScheduling
@Configuration
public class SchedulerConfig {
    private static final Logger log = LoggerFactory.getLogger(SchedulerConfig.class);

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private EventRegistrationService eventRegistrationService;

    @Autowired
    private EmailService emailService;

    @Scheduled(cron = "${event.status.cron}")
    public void updateEventStatus() {
        log.info("Event updateEventStatus started");

        List<Long> statedEvent = eventRepository.findStartedEventsWithStatus(EventStatus.WAIT_START);
        statedEvent.forEach(eventId ->
                eventRepository.changeEventStatus(eventId, EventStatus.STARTED)
        );

        Map<Long, List<String>> eventMap = statedEvent.stream()
                .collect(Collectors.toMap(
                        eventId -> eventId,
                        eventId -> eventRegistrationService.getUserOnEvent(eventId)
                ));

        List<Long> endedEvent = eventRepository.findEndedEventsWithStatus(EventStatus.STARTED);
        endedEvent.forEach(eventId ->
                eventRepository.changeEventStatus(eventId, EventStatus.FINISHED));
    }
}
