package com.cricketacademy.academymanagementsystem.repository;

import com.cricketacademy.academymanagementsystem.entity.Batch;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BatchRepository extends JpaRepository<Batch, Long> {
}