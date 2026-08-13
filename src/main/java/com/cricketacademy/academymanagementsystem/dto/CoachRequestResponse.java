package com.cricketacademy.academymanagementsystem.dto;

import com.cricketacademy.academymanagementsystem.entity.RequestStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class CoachRequestResponse {
    private Long id;
    private String username;
    private String specialization;
    private String bio;
    private RequestStatus status;
    private LocalDateTime requestedAt;
}