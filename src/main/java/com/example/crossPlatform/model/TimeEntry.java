package com.example.crossPlatform.model;

import java.time.LocalDateTime;

import com.example.crossPlatform.enums.TaskType;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "time_entry")
public class TimeEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private Student student;
    private TaskType type;
    private String description;
    private LocalDateTime start;
    private LocalDateTime timeEnd;
    private boolean isBillable; // учёт времени
}
