package com.cricketacademy.academymanagementsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BatchResponse {
    private Long id;
    private String name;
    private String schedule;
}