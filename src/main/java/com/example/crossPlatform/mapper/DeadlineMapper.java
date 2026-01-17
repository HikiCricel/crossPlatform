package com.example.crossPlatform.mapper;

import java.util.HashSet;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.example.crossPlatform.dto.DeadlinePrediction;
import com.example.crossPlatform.dto.DeadlineRequestDTO;
import com.example.crossPlatform.dto.DeadlineResponseDTO;
import com.example.crossPlatform.enums.RiskLevel;
import com.example.crossPlatform.model.Deadline;
import com.example.crossPlatform.model.Student;
import com.example.crossPlatform.repository.TimeEntryRepository;
import com.example.crossPlatform.service.TaskNormingService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class DeadlineMapper {
    private final TaskNormingService taskNormingService;
    private final TimeEntryRepository timeEntryRepository;

    public static Deadline deadlineRequestToDeadline(DeadlineRequestDTO request) {
        return new Deadline(null, request.subject(), request.type(), request.deadline(), new HashSet<>());
    }

    public static DeadlineResponseDTO deadlineToDeadlineResponseDTO(Deadline deadline) {
        return new DeadlineResponseDTO(deadline.getId(), deadline.getSubject(), deadline.getType(),
                deadline.getDeadlineDate(),
                deadline.getStudents().stream().map(Student::getId).collect(Collectors.toSet()));
    }

    public DeadlinePrediction deadlineToDeadlinePredictionDto(Deadline deadline, Long studentId) {
        double requiredHours = taskNormingService.getRequiredHours(deadline.getType());

        Double workedHours = timeEntryRepository.sumDurationByStudentAndSubjectAndType(
                studentId, deadline.getSubject(), deadline.getType());

        workedHours = Objects.requireNonNullElse(workedHours, 0.0);

        double hoursLeft = Math.max(0, requiredHours - workedHours);

        RiskLevel risk;
        double progressPercent = (workedHours / requiredHours) * 100;
        if (progressPercent < 50) {
            risk = RiskLevel.HIGH;
        } else if (progressPercent < 80) {
            risk = RiskLevel.MEDIUM;
        } else {
            risk = RiskLevel.LOW;
        }

        return new DeadlinePrediction(deadline.getSubject(), deadline.getDeadlineDate(), hoursLeft, risk);
    }
}
