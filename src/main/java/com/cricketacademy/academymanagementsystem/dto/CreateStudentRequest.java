package com.cricketacademy.academymanagementsystem.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateStudentRequest {

    @NotNull(message = "User ID is required")
    private Long userId;

    private String parentContactNumber;
}