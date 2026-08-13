package com.cricketacademy.academymanagementsystem.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AssignCoachToBatchRequest {

    @NotNull(message = "Coach ID is required")
    private Long coachId;

    @NotNull(message = "Batch ID is required")
    private Long batchId;
}