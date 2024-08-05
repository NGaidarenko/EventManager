package com.example.eventmanager.mapper;

import com.example.eventmanager.domain.Location;
import com.example.eventmanager.entity.LocationEntity;
import org.springframework.stereotype.Component;

@Component
public class LocationEntityMapper {
    public LocationEntity toEntity(Location location) {
        return new LocationEntity(
                location.id(),
                location.name(),
                location.address(),
                location.capacity(),
                location.description()
        );
    }

    public Location toDomain(LocationEntity locationEntity) {
        return new Location(
                locationEntity.getId(),
                locationEntity.getName(),
                locationEntity.getAddress(),
                locationEntity.getCapacity(),
                locationEntity.getDescription()
        );
    }
}
