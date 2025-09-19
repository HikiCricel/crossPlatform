package com.example.crossPlatform.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.crossPlatform.model.DeadLinePredictionDTO;
import com.example.crossPlatform.model.RiskLevel;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class DeadLineController {
    private List<DeadLinePredictionDTO> deadLines = new ArrayList<>(Arrays.asList(
    new DeadLinePredictionDTO("Программирование", LocalDateTime.now(), 228.14, RiskLevel.MEDIUM),
    new DeadLinePredictionDTO("Физика", LocalDateTime.now(), 14.88, RiskLevel.HIGH)    
    ));

    @GetMapping("/deadLines")
    public List<DeadLinePredictionDTO> getDeadLines() {
        return deadLines;
    }

    @GetMapping("/deadLines/{id}")
    public ResponseEntity<DeadLinePredictionDTO> getDeadLine(@PathVariable String subject) {
        for (DeadLinePredictionDTO deadLine : deadLines) {
            if (deadLine.getSubject().equals(subject)) {
                return ResponseEntity.ok(deadLine);
            }
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/deadLines")
    public ResponseEntity<DeadLinePredictionDTO> addDeadLine(@RequestBody @Valid DeadLinePredictionDTO deadLine) {
        deadLine.setRisk(RiskLevel.LOW);
        deadLines.add(deadLine);
        return ResponseEntity.status(HttpStatus.CREATED).body(deadLine);
    }
}
