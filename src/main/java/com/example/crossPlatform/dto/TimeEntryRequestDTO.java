package com.example.crossPlatform.dto;

import com.example.crossPlatform.enums.TaskType;

public record TimeEntryRequestDTO(Long studentId, TaskType type, String subject, String description, boolean isBillable) {

}
