package com.cricketacademy.academymanagementsystem.controller;

import com.cricketacademy.academymanagementsystem.dto.AttendanceResponse;
import com.cricketacademy.academymanagementsystem.dto.MarkAttendanceRequest;
import com.cricketacademy.academymanagementsystem.service.AttendanceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/attendance")
@RequiredArgsConstructor
public class AttendanceController {

    private final AttendanceService attendanceService;

    @PostMapping
    @PreAuthorize("hasRole('COACH')")
    public ResponseEntity<AttendanceResponse> markAttendance(
            Authentication authentication, @Valid @RequestBody MarkAttendanceRequest request) {
        return ResponseEntity.ok(attendanceService.markAttendance(authentication.getName(), request));
    }

    @GetMapping("/batch/{batchId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'COACH')")
    public ResponseEntity<List<AttendanceResponse>> getBatchAttendance(
            @PathVariable Long batchId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(attendanceService.getBatchAttendanceForDate(batchId, date));
    }

    @GetMapping("/my-attendance")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<List<AttendanceResponse>> getMyAttendance(Authentication authentication) {
        return ResponseEntity.ok(attendanceService.getMyAttendance(authentication.getName()));
    }
}