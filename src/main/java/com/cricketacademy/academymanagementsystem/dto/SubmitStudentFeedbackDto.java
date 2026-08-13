package com.cricketacademy.academymanagementsystem.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SubmitStudentFeedbackDto {

    @NotBlank(message = "Comment is required")
    private String comment;
}