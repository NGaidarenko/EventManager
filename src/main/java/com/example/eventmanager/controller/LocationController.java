package com.example.eventmanager.controller;

import com.example.eventmanager.domain.Location;
import com.example.eventmanager.dto.LocationDto;
import com.example.eventmanager.mapper.LocationDtoMapper;
import com.example.eventmanager.service.LocationService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/locations")
public class LocationController {
    private static final Logger log = LoggerFactory.getLogger(LocationController.class);

    @Autowired
    private LocationService locationService;

    @Autowired
    private LocationDtoMapper locationDtoMapper;

    @GetMapping
    public ResponseEntity<List<LocationDto>> getAllLocations() {
        List<LocationDto> locations = locationService.getAllLocations()
                .stream()
                .map(locationDtoMapper::toDto)
                .toList();

        return new ResponseEntity<>(locations, HttpStatus.OK);
    }

    @GetMapping("/{locationId}")
    public ResponseEntity<LocationDto> getLocationById(@PathVariable Long locationId) {
        log.info("Get location by id: {}", locationId);
        LocationDto locationDto = locationDtoMapper.toDto(locationService.getLocationById(locationId));

        return new ResponseEntity<>(locationDto, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<LocationDto> createLocation(LocationDto newLocationDto) {
        log.info("Create new location: {}", newLocationDto);
        Location location = locationDtoMapper.toDomain(newLocationDto);
        location = locationService.saveLocation(location);

        return new ResponseEntity<>(locationDtoMapper.toDto(location), HttpStatus.CREATED);
    }

    @PutMapping("/{locationId}")
    public ResponseEntity<LocationDto> updateLocation(@PathVariable Long locationId,
                                                      @RequestBody @Valid LocationDto newLocationDto) {
        log.info("Update location: {}", newLocationDto);
        Location location = locationService.updateLocation(locationId, locationDtoMapper.toDomain(newLocationDto));
        return new ResponseEntity<>(locationDtoMapper.toDto(location), HttpStatus.OK);
    }

    @DeleteMapping("/{locationId}")
    public ResponseEntity<LocationDto> deleteLocation(@PathVariable Long locationId) {
        log.info("Delete location: {}", locationId);
        Location location = locationService.deleteLocation(locationId);

        return new ResponseEntity<>(locationDtoMapper.toDto(location), HttpStatus.NO_CONTENT);
    }
}
