package com.example.crossPlatform.mapper;

import com.example.crossPlatform.dto.TimeEntryRequestDTO;
import com.example.crossPlatform.dto.TimeEntryResponceDTO;
import com.example.crossPlatform.model.TimeEntry;

public class TimeEntryMapper {
    public static TimeEntry timeEntryRequestToTimeEntry(TimeEntryRequestDTO request) {
        return new TimeEntry(null, request.student(), request.type(), request.description(), null, null,
                request.isBillable());
    }

    public static TimeEntryResponceDTO timeEntryToTimeEntryResponceDTO(TimeEntry timeEntry) {
        return new TimeEntryResponceDTO(timeEntry.getId(), timeEntry.getStudent(), timeEntry.getType(),
                timeEntry.getDescription(), timeEntry.getTimeStart(), timeEntry.getTimeEnd(), timeEntry.isBillable());
    }
}
