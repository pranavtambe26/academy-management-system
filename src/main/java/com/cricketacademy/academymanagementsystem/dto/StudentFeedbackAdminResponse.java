package com.cricketacademy.academymanagementsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class StudentFeedbackAdminResponse {
    private Long id;
    private String studentUsername;
    private String coachUsername;
    private String comment;
    private LocalDateTime submittedAt;
}