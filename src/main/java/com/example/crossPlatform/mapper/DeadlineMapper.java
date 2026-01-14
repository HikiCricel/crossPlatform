package com.example.crossPlatform.mapper;

import java.util.HashSet;

import com.example.crossPlatform.dto.DeadlineRequestDTO;
import com.example.crossPlatform.dto.DeadlineResponseDTO;
import com.example.crossPlatform.model.Deadline;

public class DeadlineMapper {
    public static Deadline deadlineRequestToDeadline(DeadlineRequestDTO request) {
        return new Deadline(null, request.subject(), null, request.deadline(), new HashSet<>());
    }

    public static DeadlineResponseDTO deadlineToDeadlineResponseDTO(Deadline deadline) {
        return new DeadlineResponseDTO(deadline.getId(), deadline.getSubject(), deadline.getType(),
                deadline.getDeadlineDate(), deadline.getStudents());
    }
}
