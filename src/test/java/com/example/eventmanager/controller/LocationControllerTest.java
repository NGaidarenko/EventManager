package com.example.eventmanager.controller;

import com.example.eventmanager.domain.Location;
import com.example.eventmanager.dto.LocationDto;
import com.example.eventmanager.mapper.LocationDtoMapper;
import com.example.eventmanager.service.LocationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LocationControllerTest {
    @Mock
    private LocationService locationService;

    @Mock
    private LocationDtoMapper locationDtoMapper;

    @InjectMocks
    private LocationController locationController;

    private Location location;
    private LocationDto locationDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        location = new Location(1L, "Location Name", "Location Address", 100L, "Description");
        locationDto = new LocationDto(null, "Location Name", "Location Address", 100L, "Description");
    }

    @Test
    void testGetAllLocations() {
        when(locationService.getAllLocations()).thenReturn(List.of(location));
        when(locationDtoMapper.toDto(any(Location.class))).thenReturn(locationDto);

        ResponseEntity<List<LocationDto>> response = locationController.getAllLocations();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(1);
        assertThat(response.getBody().get(0)).isEqualTo(locationDto);

        verify(locationService, times(1)).getAllLocations();
        verify(locationDtoMapper, times(1)).toDto(any(Location.class));
    }

    @Test
    void testGetLocationById() {
        when(locationService.getLocationById(1L)).thenReturn(location);
        when(locationDtoMapper.toDto(any(Location.class))).thenReturn(locationDto);

        ResponseEntity<LocationDto> response = locationController.getLocationById(1L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(locationDto);

        verify(locationService, times(1)).getLocationById(1L);
        verify(locationDtoMapper, times(1)).toDto(any(Location.class));
    }

    @Test
    void testCreateLocation() {
        when(locationDtoMapper.toDomain(any(LocationDto.class))).thenReturn(location);
        when(locationService.saveLocation(any(Location.class))).thenReturn(location);
        when(locationDtoMapper.toDto(any(Location.class))).thenReturn(locationDto);

        ResponseEntity<LocationDto> response = locationController.createLocation(locationDto);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isEqualTo(locationDto);

        verify(locationService, times(1)).saveLocation(any(Location.class));
        verify(locationDtoMapper, times(1)).toDomain(any(LocationDto.class));
        verify(locationDtoMapper, times(1)).toDto(any(Location.class));
    }

    @Test
    void testUpdateLocation() {
        when(locationDtoMapper.toDomain(any(LocationDto.class))).thenReturn(location);
        when(locationService.updateLocation(eq(1L), any(Location.class))).thenReturn(location);
        when(locationDtoMapper.toDto(any(Location.class))).thenReturn(locationDto);

        ResponseEntity<LocationDto> response = locationController.updateLocation(1L, locationDto);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(locationDto);

        verify(locationService, times(1)).updateLocation(eq(1L), any(Location.class));
        verify(locationDtoMapper, times(1)).toDomain(any(LocationDto.class));
        verify(locationDtoMapper, times(1)).toDto(any(Location.class));
    }

    @Test
    void testDeleteLocation() {
        when(locationService.deleteLocation(1L)).thenReturn(location);
        when(locationDtoMapper.toDto(any(Location.class))).thenReturn(locationDto);

        ResponseEntity<LocationDto> response = locationController.deleteLocation(1L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        verify(locationService, times(1)).deleteLocation(1L);
        verify(locationDtoMapper, times(1)).toDto(any(Location.class));
    }
}
