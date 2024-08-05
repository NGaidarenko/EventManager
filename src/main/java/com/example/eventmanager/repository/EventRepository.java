package com.example.eventmanager.repository;

import com.example.eventmanager.domain.EventStatus;
import com.example.eventmanager.dto.EventSearchFilter;
import com.example.eventmanager.entity.EventEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

public interface EventRepository extends JpaRepository<EventEntity, Long> {

    @Modifying
    @Transactional
    @Query("""
        update EventEntity e set e.status= :status where e.id = :id
    """)
    void changeEventStatus(@Param("id") Long eventId,
                           @Param("status")EventStatus status);

    @Query("""
        select e from EventEntity e
        where (:name is null or e.name = :se)
        and (:minPlaces is null or e.maxPlaces > :minPlaces)
        and (:maxPlaces is null or e.maxPlaces < :maxPlaces)
        and (:minCost is null or e.cost > :minCost)
        and (:maxCost is null or e.cost < :maxCost)
        and (:minDuration is null or e.duration > :minDuration)
        and (:maxDuration is null or e.duration < :maxDuration)
        and (:locationId is null or e.locationId = :locationId)
        and (:status is null or e.status = :status)
    """)
    List<EventEntity> searchEvents(
            @Param("name") String name,
            @Param("minPlaces") Integer minPlaces,
            @Param("maxPlaces") Integer maxPlaces,
            @Param("dateAfter") LocalDateTime dateAfter,
            @Param("dateBefore") LocalDateTime dateBefore,
            @Param("minCost") Integer minCost,
            @Param("maxCost") Integer maxCost,
            @Param("minDuration") Integer minDuration,
            @Param("maxDuration") Integer maxDuration,
            @Param("locationId") Long locationId,
            @Param("status") EventStatus status
            );

    List<EventEntity> findAllByOwnerId(Long ownerId);
}
