package com.cricketacademy.academymanagementsystem.controller;

import com.cricketacademy.academymanagementsystem.dto.GiveRatingRequest;
import com.cricketacademy.academymanagementsystem.dto.PerformanceRatingResponse;
import com.cricketacademy.academymanagementsystem.service.PerformanceRatingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/performance-ratings")
@RequiredArgsConstructor
public class PerformanceRatingController {

    private final PerformanceRatingService ratingService;

    @PostMapping
    @PreAuthorize("hasRole('COACH')")
    public ResponseEntity<PerformanceRatingResponse> giveRating(
            Authentication authentication, @Valid @RequestBody GiveRatingRequest request) {
        return new ResponseEntity<>(ratingService.giveRating(authentication.getName(), request), HttpStatus.CREATED);
    }

    @GetMapping("/my-ratings")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<List<PerformanceRatingResponse>> getMyRatings(Authentication authentication) {
        return ResponseEntity.ok(ratingService.getMyRatings(authentication.getName()));
    }
}