package com.example.crossPlatform.dto;

import java.util.List;

import com.example.crossPlatform.model.TimeEntry;

public record StudentResponceDTO(Long id, String name, String group, List<TimeEntry> recentEntries) {

}
