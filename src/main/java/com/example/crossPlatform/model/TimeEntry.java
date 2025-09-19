package com.example.crossPlatform.model;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class TimeEntry {
    private Long id;
    private Student student;
    private TaskType type;
    private String description;
    private LocalDateTime start;
    private LocalDateTime end;
    private boolean isBillable; //учёт времени
}
