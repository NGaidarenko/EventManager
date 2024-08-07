package com.example.eventmanager.config;

import com.example.eventmanager.domain.EventStatus;
import com.example.eventmanager.entity.EventEntity;
import com.example.eventmanager.repository.EventRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;

@EnableScheduling
@Configuration
public class SchedulerConfig {
    private static final Logger log = LoggerFactory.getLogger(SchedulerConfig.class);

    @Autowired
    private EventRepository eventRepository;

    @Scheduled(cron = "${event.status.cron}")
    public void updateEventStatus() {
        log.info("Event updateEventStatus started");

        List<Long> statedEvent = eventRepository.findStartedEventsWithStatus(EventStatus.WAIT_START);
        statedEvent.forEach(eventId ->
                eventRepository.changeEventStatus(eventId, EventStatus.STARTED)
        );

        List<Long> endedEvent = eventRepository.findEndedEventsWithStatus(EventStatus.STARTED);
        statedEvent.forEach(eventId ->
                eventRepository.changeEventStatus(eventId, EventStatus.FINISHED));
    }
}
