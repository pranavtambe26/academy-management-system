package com.cricketacademy.academymanagementsystem.service;

import com.cricketacademy.academymanagementsystem.dto.AssignCoachToBatchRequest;
import com.cricketacademy.academymanagementsystem.dto.CoachBatchAssignmentResponse;
import com.cricketacademy.academymanagementsystem.entity.Batch;
import com.cricketacademy.academymanagementsystem.entity.Coach;
import com.cricketacademy.academymanagementsystem.entity.CoachBatchAssignment;
import com.cricketacademy.academymanagementsystem.repository.BatchRepository;
import com.cricketacademy.academymanagementsystem.repository.CoachBatchAssignmentRepository;
import com.cricketacademy.academymanagementsystem.repository.CoachRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CoachBatchAssignmentService {

    private final CoachBatchAssignmentRepository assignmentRepository;
    private final CoachRepository coachRepository;
    private final BatchRepository batchRepository;

    public CoachBatchAssignmentResponse assignCoachToBatch(AssignCoachToBatchRequest request) {

        Coach coach = coachRepository.findById(request.getCoachId())
                .orElseThrow(() -> new IllegalArgumentException("Coach not found with id: " + request.getCoachId()));

        Batch batch = batchRepository.findById(request.getBatchId())
                .orElseThrow(() -> new IllegalArgumentException("Batch not found with id: " + request.getBatchId()));

        if (assignmentRepository.existsByCoach_IdAndBatch_Id(coach.getId(), batch.getId())) {
            throw new IllegalArgumentException("This coach is already assigned to this batch");
        }

        CoachBatchAssignment assignment = CoachBatchAssignment.builder()
                .coach(coach)
                .batch(batch)
                .assignedDate(LocalDate.now())
                .build();

        CoachBatchAssignment saved = assignmentRepository.save(assignment);

        return new CoachBatchAssignmentResponse(saved.getId(), coach.getUser().getUsername(),
                batch.getName(), saved.getAssignedDate());
    }

    public List<CoachBatchAssignmentResponse> getBatchesForCoach(Long coachId) {
        return assignmentRepository.findByCoach_Id(coachId).stream()
                .map(a -> new CoachBatchAssignmentResponse(a.getId(), a.getCoach().getUser().getUsername(),
                        a.getBatch().getName(), a.getAssignedDate()))
                .toList();
    }

    public List<CoachBatchAssignmentResponse> getCoachesForBatch(Long batchId) {
        return assignmentRepository.findByBatch_Id(batchId).stream()
                .map(a -> new CoachBatchAssignmentResponse(a.getId(), a.getCoach().getUser().getUsername(),
                        a.getBatch().getName(), a.getAssignedDate()))
                .toList();
    }

    public void removeAssignment(Long assignmentId) {
        if (!assignmentRepository.existsById(assignmentId)) {
            throw new IllegalArgumentException("Assignment not found with id: " + assignmentId);
        }
        assignmentRepository.deleteById(assignmentId);
    }

    public List<CoachBatchAssignmentResponse> getMyBatches(String username) {
        Coach coach = coachRepository.findByUser_Username(username)
                .orElseThrow(() -> new IllegalArgumentException("Coach profile not found for this user"));

        return getBatchesForCoach(coach.getId());
    }
}