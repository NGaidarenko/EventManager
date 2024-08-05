package com.example.eventmanager.entity;

import com.example.eventmanager.domain.EventStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "events", schema = "event-manage")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "owner_id", nullable = false)
    private Long ownerId;

    @Column(name = "max_places", nullable = false)
    private int maxPlaces;

    @OneToMany(mappedBy = "event")
    private List<EventRegistrationEntity> registrationList;

    @Column(name = "date", nullable = false)
    private LocalDateTime date;

    @Column(name = "cost", nullable = false)
    private int cost;

    @Column(name = "duration", nullable = false)
    private int duration;

    @Column(name = "location_id", nullable = false)
    private Long locationId;

    @Column(name = "status", nullable = false)
    private EventStatus status;


}
