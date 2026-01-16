package com.example.crossPlatform.dto;

import java.util.List;
import java.util.Set;

import com.example.crossPlatform.model.Deadline;

public record StudentResponseDTO(Long id, String name, String group, List<TimeEntryResponseDTO> recentEntries, Set<Deadline> deadlines) {

}
