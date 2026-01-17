package com.example.crossPlatform.dto;

import java.time.LocalDate;

import com.example.crossPlatform.enums.RiskLevel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class DeadlinePrediction {
    private String subject;
    private LocalDate deadline;
    private Double hoursLeft;
    private RiskLevel risk;
}
