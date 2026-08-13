package com.cricketacademy.academymanagementsystem.repository;

import com.cricketacademy.academymanagementsystem.entity.StudentBatchAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface StudentBatchAssignmentRepository extends JpaRepository<StudentBatchAssignment, Long> {

    List<StudentBatchAssignment> findByStudent_Id(Long studentId);

    List<StudentBatchAssignment> findByBatch_Id(Long batchId);

    boolean existsByStudent_IdAndBatch_Id(Long studentId, Long batchId);
}