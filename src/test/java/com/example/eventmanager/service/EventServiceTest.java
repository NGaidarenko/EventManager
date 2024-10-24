package com.example.eventmanager.service;

import com.example.eventmanager.domain.*;
import com.example.eventmanager.dto.EventCreateRequestDto;
import com.example.eventmanager.dto.EventUpdateRequestDto;
import com.example.eventmanager.entity.EventEntity;
import com.example.eventmanager.mapper.EventMapper;
import com.example.eventmanager.repository.EventRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EventServiceTest {

    @Mock
    private EventRepository eventRepository;

    @Mock
    private LocationService locationService;

    @Mock
    private UserAuthenticationService authenticationService;

    @Mock
    private EventMapper eventMapper;

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private EventService eventService;

    private EventEntity eventEntity;
    private Event event;
    private EventCreateRequestDto createRequestDto;
    private User currentUser;

    @BeforeEach
    void setUp() {
        currentUser = new User(1L, "john.doe@example.com", 20, Role.USER, "test");
        eventEntity = new EventEntity(1L, "Test Event", 1L, 50, List.of(), LocalDateTime.now(), 500, 200, 1L, EventStatus.WAIT_START);
        event = new Event(1L, "Test Event", 1L, 50, List.of(), LocalDateTime.now(), 500, 200, 1L, EventStatus.WAIT_START);
        createRequestDto = new EventCreateRequestDto("New Event", 50, LocalDateTime.now(), 100, 200, 1L);
    }

    @Test
    void testCreateEventSuccess() {
        Location location = new Location(1L, "Test Location", "Test Address", 100L, "Test Description");
        when(locationService.getLocationById(1L)).thenReturn(location);
        when(authenticationService.getCurrentAuthenticatedUser()).thenReturn(currentUser);
        when(eventRepository.save(any(EventEntity.class))).thenReturn(eventEntity);
        when(eventMapper.toDomain(any(EventEntity.class))).thenReturn(event);

        Event createdEvent = eventService.createEvent(createRequestDto);

        assertThat(createdEvent).isEqualTo(event);
        verify(eventRepository, times(1)).save(any(EventEntity.class));
        verify(notificationService, never()).changeEventStatus(anyLong(), any(EventStatus.class)); // Не вызывается при создании
    }

    @Test
    void testCreateEventLocationCapacityExceeded() {
        Location location = new Location(1L, "Test Location", "Test Address", 30L, "Test Description"); // Меньшая вместимость
        when(locationService.getLocationById(1L)).thenReturn(location);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            eventService.createEvent(createRequestDto);
        });

        assertThat(exception.getMessage()).isEqualTo("Capacity of location is: 30, but maxPlaces is: 50");
        verify(eventRepository, never()).save(any(EventEntity.class));
    }

    @Test
    void testGetEventByIdSuccess() {
        when(eventRepository.findById(1L)).thenReturn(Optional.of(eventEntity));
        when(eventMapper.toDomain(any(EventEntity.class))).thenReturn(event);

        Event foundEvent = eventService.getEventById(1L);

        assertThat(foundEvent).isEqualTo(event);
        verify(eventRepository, times(1)).findById(1L);
    }

    @Test
    void testGetEventByIdNotFound() {
        when(eventRepository.findById(1L)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            eventService.getEventById(1L);
        });

        assertThat(exception.getMessage()).isEqualTo("Cannot found event with id: 1");
        verify(eventRepository, times(1)).findById(1L);
    }

    @Test
    void testCancelEventSuccess() {
        when(eventRepository.findById(1L)).thenReturn(Optional.of(eventEntity));
        when(authenticationService.getCurrentAuthenticatedUser()).thenReturn(currentUser);
        when(eventMapper.toDomain(eventEntity)).thenReturn(event);

        eventService.cancelEvent(1L);

        verify(eventRepository, times(1)).changeEventStatus(1L, EventStatus.CANCELLED);
        verify(notificationService, times(1)).changeEventStatus(1L, EventStatus.CANCELLED);
    }

    @Test
    void testUpdateEventSuccess() {
        EventUpdateRequestDto updateRequest = new EventUpdateRequestDto("Updated Event", 60, LocalDateTime.now(), 120, 3, 1L);
        when(eventRepository.findById(1L)).thenReturn(Optional.of(eventEntity));
        when(authenticationService.getCurrentAuthenticatedUser()).thenReturn(currentUser);
        when(eventMapper.toDomain(any(EventEntity.class))).thenReturn(event);
        when(locationService.getLocationById(anyLong())).thenReturn(new Location(1L, "Test Location", "Test address", 120L, "Test description"));

        Event updatedEvent = eventService.updateEvent(1L, updateRequest);

        assertThat(updatedEvent).isEqualTo(event);
        verify(eventRepository, times(1)).save(any(EventEntity.class));
        verify(notificationService, times(1)).changeEventFields(any(EventEntity.class), eq(updateRequest));
    }

    @Test
    void testGetUserEventsSuccess() {
        when(authenticationService.getCurrentAuthenticatedUser()).thenReturn(currentUser);
        when(eventRepository.findAllByOwnerId(1L)).thenReturn(List.of(eventEntity));
        when(eventMapper.toDomain(any(EventEntity.class))).thenReturn(event);

        List<Event> userEvents = eventService.getUserEvents();

        assertThat(userEvents).hasSize(1);
        assertThat(userEvents.get(0)).isEqualTo(event);
        verify(eventRepository, times(1)).findAllByOwnerId(1L);
    }
}
