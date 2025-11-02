package com.example.crossPlatform.dto;

import java.time.LocalDateTime;

import com.example.crossPlatform.enums.RiskLevel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class DeadlinePrediction {
    private String subject;
    private LocalDateTime deadline;
    private Double hoursLeft;
    private RiskLevel risk;
}
