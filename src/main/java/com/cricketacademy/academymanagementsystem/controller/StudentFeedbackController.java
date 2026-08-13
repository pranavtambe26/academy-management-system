package com.cricketacademy.academymanagementsystem.controller;

import com.cricketacademy.academymanagementsystem.dto.StudentFeedbackAdminResponse;
import com.cricketacademy.academymanagementsystem.dto.SubmitStudentFeedbackDto;
import com.cricketacademy.academymanagementsystem.service.StudentFeedbackService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/student-feedback")
@RequiredArgsConstructor
public class StudentFeedbackController {

    private final StudentFeedbackService feedbackService;

    @PostMapping("/coach/{coachId}")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<Void> submitFeedback(
            Authentication authentication, @PathVariable Long coachId, @Valid @RequestBody SubmitStudentFeedbackDto dto) {
        feedbackService.submitFeedback(authentication.getName(), coachId, dto);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/coach/{coachId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<StudentFeedbackAdminResponse>> getFeedbackForCoach(@PathVariable Long coachId) {
        return ResponseEntity.ok(feedbackService.getAllFeedbackForCoach(coachId));
    }
}