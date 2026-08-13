package com.cricketacademy.academymanagementsystem.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class GiveRatingRequest {

    @NotNull(message = "Student ID is required")
    private Long studentId;

    @NotNull(message = "Batch ID is required")
    private Long batchId;

    @NotNull(message = "Rating is required")
    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 10, message = "Rating must be at most 10")
    private Integer rating;

    private String remarks;
}