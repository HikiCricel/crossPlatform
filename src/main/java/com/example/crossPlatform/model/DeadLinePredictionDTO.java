package com.example.crossPlatform.model;

import java.time.LocalDateTime;

import com.example.crossPlatform.enums.RiskLevel;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class DeadLinePredictionDTO {
    private String subject;
    private LocalDateTime deadline;
    private Double hoursLeft;
    private RiskLevel risk;
}
