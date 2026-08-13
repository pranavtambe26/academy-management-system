package com.cricketacademy.academymanagementsystem.controller;

import com.cricketacademy.academymanagementsystem.dto.CoachRequestResponse;
import com.cricketacademy.academymanagementsystem.dto.ReviewRequestDto;
import com.cricketacademy.academymanagementsystem.dto.SubmitCoachRequestDto;
import com.cricketacademy.academymanagementsystem.entity.RequestStatus;
import com.cricketacademy.academymanagementsystem.service.CoachRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/coach-requests")
@RequiredArgsConstructor
public class CoachRequestController {

    private final CoachRequestService coachRequestService;

    @PostMapping
    @PreAuthorize("hasRole('COACH')")
    public ResponseEntity<CoachRequestResponse> submitRequest(
            Authentication authentication,
            @RequestBody SubmitCoachRequestDto dto) {

        CoachRequestResponse response = coachRequestService.submitRequest(authentication.getName(), dto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<CoachRequestResponse>> getRequests(
            @RequestParam(defaultValue = "PENDING") RequestStatus status) {
        return ResponseEntity.ok(coachRequestService.getRequestsByStatus(status));
    }

    @PutMapping("/{id}/approve")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CoachRequestResponse> approve(
            @PathVariable Long id, @RequestBody ReviewRequestDto dto) {
        return ResponseEntity.ok(coachRequestService.approveRequest(id, dto.getRemarks()));
    }

    @PutMapping("/{id}/reject")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CoachRequestResponse> reject(
            @PathVariable Long id, @RequestBody ReviewRequestDto dto) {
        return ResponseEntity.ok(coachRequestService.rejectRequest(id, dto.getRemarks()));
    }
}
