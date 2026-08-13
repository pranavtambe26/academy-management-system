package com.cricketacademy.academymanagementsystem.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateCoachRequest {

    @NotNull(message = "User ID is required")
    private Long userId;

    private String specialization;

    private String bio;
}