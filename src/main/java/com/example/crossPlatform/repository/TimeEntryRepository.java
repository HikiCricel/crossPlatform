package com.example.crossPlatform.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.crossPlatform.model.TimeEntry;

public interface TimeEntryRepository extends JpaRepository<TimeEntry, Long> {
    List<TimeEntry> findByDescriptionStartingWithIgnoreCase(String description);

    List<TimeEntry> findAllByDescription(String description);
}
