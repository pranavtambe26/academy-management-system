package com.cricketacademy.academymanagementsystem.repository;

import com.cricketacademy.academymanagementsystem.entity.PerformanceRating;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PerformanceRatingRepository extends JpaRepository<PerformanceRating, Long> {

    List<PerformanceRating> findByStudent_Id(Long studentId);

    List<PerformanceRating> findByBatch_Id(Long batchId);
}