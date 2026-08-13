package com.cricketacademy.academymanagementsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class CoachBatchAssignmentResponse {
    private Long id;
    private String coachUsername;
    private String batchName;
    private LocalDate assignedDate;
}