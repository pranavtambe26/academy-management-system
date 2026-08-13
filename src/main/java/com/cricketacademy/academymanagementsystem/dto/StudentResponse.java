package com.cricketacademy.academymanagementsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StudentResponse {
    private Long id;
    private String username;
    private String email;
    private String parentContactNumber;
}