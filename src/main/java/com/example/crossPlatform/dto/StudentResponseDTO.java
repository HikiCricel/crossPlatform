package com.example.crossPlatform.dto;

import java.util.List;
import java.util.Set;

public record StudentResponseDTO(Long id, String name, String group, List<TimeEntryResponseDTO> recentEntries,
                Set<Long> deadlineIds) {
        public Long getId() {
                return id;
        }

        public String getName() {
                return name;
        }

        public String getGroup() {
                return group;
        }

        public List<TimeEntryResponseDTO> getRecentEntries() {
                return recentEntries;
        }

        public Set<Long> getDeadlineIds() {
                return deadlineIds;
        }
}
