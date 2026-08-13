package com.cricketacademy.academymanagementsystem.controller;

import com.cricketacademy.academymanagementsystem.dto.ReviewRequestDto;
import com.cricketacademy.academymanagementsystem.dto.StudentBatchRequestResponse;
import com.cricketacademy.academymanagementsystem.dto.SubmitStudentBatchRequestDto;
import com.cricketacademy.academymanagementsystem.entity.RequestStatus;
import com.cricketacademy.academymanagementsystem.service.StudentBatchRequestService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/student-batch-requests")
@RequiredArgsConstructor
public class StudentBatchRequestController {

    private final StudentBatchRequestService studentBatchRequestService;

    @PostMapping
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<StudentBatchRequestResponse> submitRequest(
            Authentication authentication,
            @Valid @RequestBody SubmitStudentBatchRequestDto dto) {

        StudentBatchRequestResponse response =
                studentBatchRequestService.submitRequest(authentication.getName(), dto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<StudentBatchRequestResponse>> getRequests(
            @RequestParam(defaultValue = "PENDING") RequestStatus status) {
        return ResponseEntity.ok(studentBatchRequestService.getRequestsByStatus(status));
    }

    @PutMapping("/{id}/approve")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<StudentBatchRequestResponse> approve(
            @PathVariable Long id, @RequestBody ReviewRequestDto dto) {
        return ResponseEntity.ok(studentBatchRequestService.approveRequest(id, dto.getRemarks()));
    }

    @PutMapping("/{id}/reject")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<StudentBatchRequestResponse> reject(
            @PathVariable Long id, @RequestBody ReviewRequestDto dto) {
        return ResponseEntity.ok(studentBatchRequestService.rejectRequest(id, dto.getRemarks()));
    }
}