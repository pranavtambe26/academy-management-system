package com.cricketacademy.academymanagementsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class PerformanceRatingResponse {
    private Long id;
    private String studentUsername;
    private String batchName;
    private String coachUsername;
    private Integer rating;
    private String remarks;
    private LocalDate ratedDate;
}