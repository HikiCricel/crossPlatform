package com.example.crossPlatform.dto;

import java.util.List;
import java.util.Set;

public record StudentResponseDTO(Long id, String name, String group, List<TimeEntryResponseDTO> recentEntries,
        Set<Long> deadlineIds) {

}
