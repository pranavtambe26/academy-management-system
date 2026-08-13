package com.cricketacademy.academymanagementsystem.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateBatchRequest {

    @NotBlank(message = "Batch name is required")
    private String name;

    @NotBlank(message = "Schedule is required")
    private String schedule;
}