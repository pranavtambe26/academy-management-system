package com.cricketacademy.academymanagementsystem.dto;

import com.cricketacademy.academymanagementsystem.entity.AttendanceStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class AttendanceResponse {
    private Long id;
    private String studentUsername;
    private String batchName;
    private LocalDate date;
    private AttendanceStatus status;
    private String markedByCoachUsername;
}