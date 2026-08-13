package com.cricketacademy.academymanagementsystem.controller;

import com.cricketacademy.academymanagementsystem.dto.CoachFeedbackResponse;
import com.cricketacademy.academymanagementsystem.dto.SubmitCoachFeedbackDto;
import com.cricketacademy.academymanagementsystem.service.CoachFeedbackService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/coach-feedback")
@RequiredArgsConstructor
public class CoachFeedbackController {

    private final CoachFeedbackService feedbackService;

    @PostMapping("/student/{studentId}")
    @PreAuthorize("hasRole('COACH')")
    public ResponseEntity<CoachFeedbackResponse> submitFeedback(
            Authentication authentication, @PathVariable Long studentId, @Valid @RequestBody SubmitCoachFeedbackDto dto) {
        return new ResponseEntity<>(feedbackService.submitFeedback(authentication.getName(), studentId, dto), HttpStatus.CREATED);
    }

    @GetMapping("/my-feedback")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<List<CoachFeedbackResponse>> getMyFeedback(Authentication authentication) {
        return ResponseEntity.ok(feedbackService.getMyFeedback(authentication.getName()));
    }

    @GetMapping("/student/{studentId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<CoachFeedbackResponse>> getFeedbackForStudent(@PathVariable Long studentId) {
        return ResponseEntity.ok(feedbackService.getAllFeedbackForStudent(studentId));
    }
}