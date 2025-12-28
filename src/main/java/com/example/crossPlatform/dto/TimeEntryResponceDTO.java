package com.example.crossPlatform.dto;

import java.time.LocalDateTime;

import com.example.crossPlatform.enums.TaskType;
import com.example.crossPlatform.model.Student;

public record TimeEntryResponceDTO(Long id, Student student, TaskType taskType, String description,
        LocalDateTime timeStart, LocalDateTime timeEnd, boolean isBillable) {

}
