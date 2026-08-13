package com.cricketacademy.academymanagementsystem.dto;

import com.cricketacademy.academymanagementsystem.entity.FeedbackVisibility;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SubmitCoachFeedbackDto {

    @NotBlank(message = "Comment is required")
    private String comment;

    @NotNull(message = "Visibility is required")
    private FeedbackVisibility visibility;
}