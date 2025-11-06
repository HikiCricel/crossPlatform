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
    @Size(min = 3, max = 500, message = "description")
    @Column(nullable = false, length = 100)
    private String description;
    @Size(min = 3, max = 500, message = "starttime")
    @Column(nullable = false, length = 100)
    private LocalDateTime timeStart;
    @Size(min = 3, max = 500, message = "endtime")
    @Column(nullable = false, length = 100)
    private LocalDateTime timeEnd;
    @Column()
    private boolean isBillable; // учёт времени
}
