package com.cricketacademy.academymanagementsystem.repository;

import com.cricketacademy.academymanagementsystem.entity.CoachBatchAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CoachBatchAssignmentRepository extends JpaRepository<CoachBatchAssignment, Long> {

    List<CoachBatchAssignment> findByCoach_Id(Long coachId);

    List<CoachBatchAssignment> findByBatch_Id(Long batchId);

    boolean existsByCoach_IdAndBatch_Id(Long coachId, Long batchId);
}