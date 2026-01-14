package com.example.crossPlatform.dto;

import java.util.List;
import java.util.Set;

import com.example.crossPlatform.model.Deadline;
import com.example.crossPlatform.model.TimeEntry;

public record StudentResponseDTO(Long id, String name, String group, List<TimeEntry> recentEntries, Set<Deadline> deadlines) {

}
