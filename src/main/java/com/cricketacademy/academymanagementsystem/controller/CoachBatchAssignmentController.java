package com.cricketacademy.academymanagementsystem.controller;

import com.cricketacademy.academymanagementsystem.dto.AssignCoachToBatchRequest;
import com.cricketacademy.academymanagementsystem.dto.CoachBatchAssignmentResponse;
import com.cricketacademy.academymanagementsystem.service.CoachBatchAssignmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/coach-batch-assignments")
@RequiredArgsConstructor
public class CoachBatchAssignmentController {

    private final CoachBatchAssignmentService assignmentService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CoachBatchAssignmentResponse> assign(@Valid @RequestBody AssignCoachToBatchRequest request) {
        return new ResponseEntity<>(assignmentService.assignCoachToBatch(request), HttpStatus.CREATED);
    }

    @GetMapping("/coach/{coachId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'COACH')")
    public ResponseEntity<List<CoachBatchAssignmentResponse>> getBatchesForCoach(@PathVariable Long coachId) {
        return ResponseEntity.ok(assignmentService.getBatchesForCoach(coachId));
    }

    @GetMapping("/batch/{batchId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<CoachBatchAssignmentResponse>> getCoachesForBatch(@PathVariable Long batchId) {
        return ResponseEntity.ok(assignmentService.getCoachesForBatch(batchId));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> removeAssignment(@PathVariable Long id) {
        assignmentService.removeAssignment(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/my-batches")
    @PreAuthorize("hasRole('COACH')")
    public ResponseEntity<List<CoachBatchAssignmentResponse>> getMyBatches(Authentication authentication) {
        return ResponseEntity.ok(assignmentService.getMyBatches(authentication.getName()));
    }
}