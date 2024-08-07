package com.example.eventmanager.repository;

import com.example.eventmanager.entity.EventEntity;
import com.example.eventmanager.entity.EventRegistrationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface EventRegistrationRepository extends JpaRepository<EventRegistrationEntity, Long> {
    Optional<EventRegistrationEntity> findByEventIdAndUserId(Long eventId, Long userId);

    @Modifying
    @Transactional
    void deleteByEventIdAndUserId(Long eventId, Long userId);

    @Query("""
        select r.event FROM EventRegistrationEntity r
        WHERE r.userId = :userId
    """)
    List<EventEntity> findRegisteredEvents(@Param("userId") Long userId);
}
