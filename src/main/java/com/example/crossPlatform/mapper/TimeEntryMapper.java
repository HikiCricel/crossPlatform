package com.example.crossPlatform.mapper;

import com.example.crossPlatform.dto.TimeEntryRequestDTO;
import com.example.crossPlatform.dto.TimeEntryResponseDTO;
import com.example.crossPlatform.model.TimeEntry;

public class TimeEntryMapper {
    public static TimeEntry timeEntryRequestToTimeEntry(TimeEntryRequestDTO request) {
        return new TimeEntry(null, null, request.type(), request.subject(), request.description(), null, null, 0.0,
                request.isBillable());
    }

    public static TimeEntryResponseDTO timeEntryToTimeEntryResponseDTO(TimeEntry timeEntry) {
        return new TimeEntryResponseDTO(timeEntry.getId(), timeEntry.getStudent().getId(), timeEntry.getType(),
                timeEntry.getDescription(), timeEntry.getTimeStart(), timeEntry.getTimeEnd(), timeEntry.isBillable());
    }
}
