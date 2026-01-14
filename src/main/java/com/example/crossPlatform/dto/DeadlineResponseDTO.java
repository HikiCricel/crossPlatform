package com.example.crossPlatform.dto;

import java.time.LocalDateTime;
import java.util.Set;

import com.example.crossPlatform.enums.TaskType;
import com.example.crossPlatform.model.Student;

public record DeadlineResponseDTO(Long id, String subject, TaskType type, LocalDateTime deadline, Set<Student> students) {

}
