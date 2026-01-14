package com.example.crossPlatform.model;

import java.time.LocalDateTime;

import com.example.crossPlatform.enums.TaskType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
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
    @JoinColumn(name = "student_id")
    private Student student;
    @Size(min = 3, max = 500, message = "tasktype")
    @Column(nullable = false, length = 100)
    private TaskType type;
    @Size(min = 2, max = 100, message = "subject")
    @Column(nullable = false, length = 100)
    private String subject;
    @Size(min = 3, max = 500, message = "description")
    @Column(nullable = false, length = 100)
    private String description;
    @Column(nullable = false, length = 100)
    private LocalDateTime timeStart;
    @Column(nullable = true, length = 100)
    private LocalDateTime timeEnd;
    @Column(nullable = true, length = 100)
    private Double duration;
    @Column()
    private boolean isBillable; // учёт времени
}
