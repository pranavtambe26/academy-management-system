package com.cricketacademy.academymanagementsystem.repository;

import com.cricketacademy.academymanagementsystem.entity.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

    List<Attendance> findByStudent_Id(Long studentId);

    List<Attendance> findByBatch_IdAndDate(Long batchId, LocalDate date);

    Optional<Attendance> findByStudent_IdAndBatch_IdAndDate(Long studentId, Long batchId, LocalDate date);

    boolean existsByStudent_IdAndBatch_IdAndDate(Long studentId, Long batchId, LocalDate date);
}