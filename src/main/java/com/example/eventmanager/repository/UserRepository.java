package com.example.eventmanager.repository;

import com.example.eventmanager.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Boolean existsByLogin(String login);
    Optional<UserEntity> findByLogin(String login);
}
