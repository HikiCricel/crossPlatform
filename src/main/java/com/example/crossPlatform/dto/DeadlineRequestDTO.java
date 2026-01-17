package com.example.crossPlatform.dto;

import java.time.LocalDate;

import com.example.crossPlatform.enums.TaskType;

public record DeadlineRequestDTO(String subject, LocalDate deadline, TaskType type) {

}
