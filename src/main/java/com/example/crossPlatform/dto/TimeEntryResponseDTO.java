package com.example.crossPlatform.dto;

import java.time.LocalDateTime;

import com.example.crossPlatform.enums.TaskType;

public record TimeEntryResponseDTO(Long id, Long studentId, TaskType taskType, String subject, String description,
                LocalDateTime timeStart, LocalDateTime timeEnd, boolean isBillable) {

}
