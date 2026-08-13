package com.cricketacademy.academymanagementsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CoachResponse {
    private Long id;
    private String username;
    private String email;
    private String specialization;
    private String bio;
}