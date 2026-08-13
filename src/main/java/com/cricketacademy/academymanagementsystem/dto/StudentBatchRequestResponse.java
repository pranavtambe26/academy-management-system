package com.cricketacademy.academymanagementsystem.dto;

import com.cricketacademy.academymanagementsystem.entity.RequestStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class StudentBatchRequestResponse {
    private Long id;
    private String username;
    private String batchName;
    private String parentContactNumber;
    private RequestStatus status;
    private LocalDateTime requestedAt;
}