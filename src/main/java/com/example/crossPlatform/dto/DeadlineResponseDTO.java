package com.example.crossPlatform.dto;

import java.time.LocalDate;
import java.util.Set;

import com.example.crossPlatform.enums.TaskType;

public record DeadlineResponseDTO(Long id, String subject, TaskType type, LocalDate deadline, Set<Long> studentIds) {

}
