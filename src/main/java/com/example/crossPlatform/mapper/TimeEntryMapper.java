package com.example.crossPlatform.mapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.example.crossPlatform.dto.TimeEntryRequestDTO;
import com.example.crossPlatform.dto.TimeEntryResponseDTO;
import com.example.crossPlatform.model.TimeEntry;

public class TimeEntryMapper {
    public static TimeEntry timeEntryRequestToTimeEntry(TimeEntryRequestDTO request) {
        return new TimeEntry(null, null, request.type(), request.subject(), request.description(), LocalDateTime.now(), null, 0.0,
                request.isBillable());
    }

    public static TimeEntryResponseDTO timeEntryToTimeEntryResponseDTO(TimeEntry timeEntry) {
        return new TimeEntryResponseDTO(timeEntry.getId(), timeEntry.getStudent().getId(), timeEntry.getType(),
                timeEntry.getSubject(), timeEntry.getDescription(), timeEntry.getTimeStart(), timeEntry.getTimeEnd(), timeEntry.isBillable());
    }
    
    public static List<TimeEntryResponseDTO> timeEntryToTimeEntryResponseDTOList(List<TimeEntry> timeEntries) {
        if (timeEntries == null) {
            return List.of();
        }
        return timeEntries.stream()
                .map(TimeEntryMapper::timeEntryToTimeEntryResponseDTO)
                .collect(Collectors.toList());
    }
}
