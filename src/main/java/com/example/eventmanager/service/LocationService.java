package com.example.eventmanager.service;

import com.example.eventmanager.controller.LocationController;
import com.example.eventmanager.domain.Location;
import com.example.eventmanager.entity.LocationEntity;
import com.example.eventmanager.mapper.LocationEntityMapper;
import com.example.eventmanager.repository.LocationRepository;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LocationService {
    private static final Logger log = LoggerFactory.getLogger(LocationService.class);

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private LocationEntityMapper entityMapper;

    public List<Location> getAllLocations() {

        return locationRepository.findAll()
                .stream()
                .map(entityMapper::toDomain)
                .toList();
    }

    public Location getLocationById(Long id) {
        LocationEntity locationEntity = locationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Can not find location with id: " + id));

        return entityMapper.toDomain(locationEntity);
    }

    public Location saveLocation(Location location) {
        if (location.id() != null) {
            throw new IllegalArgumentException("Can not save location with id: " + location.id());
        }
        log.info("Saving location: {}", location);

        LocationEntity locationEntity = entityMapper.toEntity(location);
        return entityMapper.toDomain(locationRepository.save(locationEntity));
    }

    public Location updateLocation(Long id, Location location) {
        if (id == null) {
            throw new IllegalArgumentException("Can not update location with id: " + null);
        }
        if (location == null) {
            throw new IllegalArgumentException("Can not update location with null location");
        }
        log.info("Updating location: {}", location);

        LocationEntity locationEntity = locationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Can not find location with id: " + id));

        locationEntity.setName(location.name());
        locationEntity.setAddress(location.address());
        locationEntity.setCapacity(location.capacity());
        locationEntity.setDescription(location.description());
        return entityMapper.toDomain(locationRepository.save(locationEntity));
    }

    public Location deleteLocation(Long id) {
        LocationEntity locationEntity = locationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Can not find location with id: " + id));
        log.info("Deleting location: {}", locationEntity);

        locationRepository.deleteById(id);
        return entityMapper.toDomain(locationEntity);
    }
}
