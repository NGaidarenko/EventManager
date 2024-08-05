package com.example.eventmanager.mapper;

import com.example.eventmanager.domain.Location;
import com.example.eventmanager.dto.LocationDto;
import org.springframework.stereotype.Component;

@Component
public class LocationDtoMapper {
    public LocationDto toDto(Location location) {
        return new LocationDto(
                location.id(),
                location.name(),
                location.address(),
                location.capacity(),
                location.description()
        );
    }

    public Location toDomain(LocationDto locationDto) {
        return new Location(
                locationDto.id(),
                locationDto.name(),
                locationDto.address(),
                locationDto.capacity(),
                locationDto.description()
        );
    }
}
