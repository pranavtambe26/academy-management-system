package com.cricketacademy.academymanagementsystem.repository;

import com.cricketacademy.academymanagementsystem.entity.Coach;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CoachRepository extends JpaRepository<Coach, Long> {

    Optional<Coach> findByUser_Username(String username);

    Optional<Coach> findByUserId(Long userId);
}