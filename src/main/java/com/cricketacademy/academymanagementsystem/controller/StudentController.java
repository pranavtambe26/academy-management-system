package com.cricketacademy.academymanagementsystem.controller;

import com.cricketacademy.academymanagementsystem.dto.CreateStudentRequest;
import com.cricketacademy.academymanagementsystem.dto.StudentResponse;
import com.cricketacademy.academymanagementsystem.service.StudentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<StudentResponse> createStudentProfile(@Valid @RequestBody CreateStudentRequest request) {
        return new ResponseEntity<>(studentService.createStudentProfile(request), HttpStatus.CREATED);
    }
}