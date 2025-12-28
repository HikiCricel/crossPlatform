package com.example.crossPlatform.dto;

import com.example.crossPlatform.enums.TaskType;
import com.example.crossPlatform.model.Student;

public record TimeEntryRequestDTO(Student student, TaskType type, String description, boolean isBillable) {

}
