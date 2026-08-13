package com.cricketacademy.academymanagementsystem.dto;

import com.cricketacademy.academymanagementsystem.entity.FeedbackVisibility;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class CoachFeedbackResponse {
    private Long id;
    private String coachUsername;
    private String comment;
    private FeedbackVisibility visibility;
    private LocalDateTime submittedAt;
}