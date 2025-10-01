package com.example.crossPlatform.model;

import java.time.LocalDateTime;

import com.example.crossPlatform.enums.TaskType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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
    @NotBlank
    @Size(min = 3, max = 500, message = "description")
    @Column(nullable = false, unique = true, length = 100)
    private String description;
    @Column(nullable = false, unique = true, length = 100)
    private LocalDateTime start;
    @Column(nullable = false, unique = true, length = 100)
    private LocalDateTime timeEnd;
    @Column(nullable = false, unique = true, length = 100)
    private boolean isBillable; // учёт времени
}
