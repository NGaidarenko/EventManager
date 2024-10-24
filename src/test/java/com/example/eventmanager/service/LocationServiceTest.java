package com.example.eventmanager.service;

import com.example.eventmanager.domain.Location;
import com.example.eventmanager.entity.LocationEntity;
import com.example.eventmanager.mapper.LocationEntityMapper;
import com.example.eventmanager.repository.LocationRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@Testcontainers
@ExtendWith(MockitoExtension.class)
public class LocationServiceTest {
    @Mock
    private LocationRepository locationRepository;

    @Mock
    private LocationEntityMapper entityMapper;

    @InjectMocks
    private LocationService locationService;

    private Location location;
    private LocationEntity locationEntity;

    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:16.2");

    @DynamicPropertySource
    static void configurationProperty(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
    }

    @BeforeAll
    static void beforeAll() {
        postgreSQLContainer.start();
    }

    @AfterAll
    static void afterAll() {
        postgreSQLContainer.stop();
    }

    @BeforeEach
    void setUp() {
//        MockitoAnnotations.openMocks(this);

        location = new Location(1L, "Location Name", "Location Address", 100L, "Description");
        locationEntity = new LocationEntity();
        locationEntity.setId(1L);
        locationEntity.setName("Location Name");
        locationEntity.setAddress("Location Address");
        locationEntity.setCapacity(100L);
        locationEntity.setDescription("Description");
    }

    @Test
    void testGetAllLocations() {
        when(locationRepository.findAll()).thenReturn(List.of(locationEntity));
        when(entityMapper.toDomain(locationEntity)).thenReturn(location);

        List<Location> locations = locationService.getAllLocations();

        assertThat(locations).hasSize(1);
        assertThat(locations.get(0)).isEqualTo(location);
        verify(locationRepository, times(1)).findAll();
        verify(entityMapper, times(1)).toDomain(locationEntity);
    }

    @Test
    void testGetLocationById() {
        when(locationRepository.findById(1L)).thenReturn(Optional.of(locationEntity));
        when(entityMapper.toDomain(locationEntity)).thenReturn(location);

        Location result = locationService.getLocationById(1L);

        assertThat(result).isEqualTo(location);
        verify(locationRepository, times(1)).findById(1L);
        verify(entityMapper, times(1)).toDomain(locationEntity);
    }

    @Test
    void testGetLocationByIdThrowsEntityNotFoundException() {
        when(locationRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> locationService.getLocationById(1L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Can not find location with id: 1");

        verify(locationRepository, times(1)).findById(1L);
    }

    @Test
    void testSaveLocation() {
        when(entityMapper.toEntity(any(Location.class))).thenReturn(locationEntity);
        when(locationRepository.save(any(LocationEntity.class))).thenReturn(locationEntity);
        when(entityMapper.toDomain(any(LocationEntity.class))).thenReturn(location);

        Location result = locationService.saveLocation(new Location(null, "New Location", "New Address", 200L, "New Description"));

        assertThat(result).isEqualTo(location);
        verify(locationRepository, times(1)).save(any(LocationEntity.class));
    }

    @Test
    void testSaveLocationThrowsExceptionWhenIdIsNotNull() {
        assertThatThrownBy(() -> locationService.saveLocation(location))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Can not save location with id: 1");

        verify(locationRepository, never()).save(any(LocationEntity.class));
    }

    @Test
    void testUpdateLocation() {
        when(locationRepository.findById(1L)).thenReturn(Optional.of(locationEntity));
        when(locationRepository.save(any(LocationEntity.class))).thenReturn(locationEntity);
        when(entityMapper.toDomain(any(LocationEntity.class))).thenReturn(location);

        Location updatedLocation = locationService.updateLocation(1L, new Location(null, "Updated Name", "Updated Address", 150L, "Updated Description"));

        assertThat(updatedLocation).isEqualTo(location);
        verify(locationRepository, times(1)).findById(1L);
        verify(locationRepository, times(1)).save(any(LocationEntity.class));
    }

    @Test
    void testUpdateLocationThrowsEntityNotFoundException() {
        when(locationRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> locationService.updateLocation(1L, location))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Can not find location with id: 1");

        verify(locationRepository, times(1)).findById(1L);
    }

    @Test
    void testDeleteLocation() {
        when(locationRepository.findById(1L)).thenReturn(Optional.of(locationEntity));
        when(entityMapper.toDomain(locationEntity)).thenReturn(location);

        Location result = locationService.deleteLocation(1L);

        assertThat(result).isEqualTo(location);
        verify(locationRepository, times(1)).findById(1L);
        verify(locationRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteLocationThrowsEntityNotFoundException() {
        when(locationRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> locationService.deleteLocation(1L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Can not find location with id: 1");

        verify(locationRepository, times(1)).findById(1L);
    }
}
