package com.cricketacademy.academymanagementsystem.controller;

import com.cricketacademy.academymanagementsystem.dto.CoachResponse;
import com.cricketacademy.academymanagementsystem.dto.CreateCoachRequest;
import com.cricketacademy.academymanagementsystem.service.CoachService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/coaches")
@RequiredArgsConstructor
public class CoachController {

    private final CoachService coachService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CoachResponse> createCoachProfile(@Valid @RequestBody CreateCoachRequest request) {
        return new ResponseEntity<>(coachService.createCoachProfile(request), HttpStatus.CREATED);
    }
}