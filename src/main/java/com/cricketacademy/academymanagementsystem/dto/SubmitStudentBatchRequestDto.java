package com.cricketacademy.academymanagementsystem.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SubmitStudentBatchRequestDto {

    @NotNull(message = "Batch ID is required")
    private Long batchId;

    private String parentContactNumber;
}