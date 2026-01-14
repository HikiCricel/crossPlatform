package com.example.crossPlatform.model;

import java.time.LocalDateTime;
import java.util.Set;

import com.example.crossPlatform.enums.TaskType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "deadlines")
public class Deadline {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(min = 2, max = 100, message = "subject")
    @Column(nullable = false, length = 100)
    private String subject;

    @Column(nullable = false, length = 100)
    @Enumerated(EnumType.STRING)
    private TaskType type;

    @Size(min = 2, max = 100, message = "deadlineDate")
    @Column(nullable = false, length = 100)
    private LocalDateTime deadlineDate;

    @ManyToMany(mappedBy = "deadlines")
    private Set<Student> students;
}
