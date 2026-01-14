package com.example.crossPlatform.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.example.crossPlatform.enums.TaskType;
import com.example.crossPlatform.model.Student;
import com.example.crossPlatform.model.TimeEntry;

@Repository
public interface TimeEntryRepository extends JpaRepository<TimeEntry, Long>, JpaSpecificationExecutor<TimeEntry> {
    List<TimeEntry> findByDescriptionStartingWithIgnoreCase(String description);

    List<TimeEntry> findAllByDescription(String description);

    List<TimeEntry> findAllByType(TaskType type);

    List<TimeEntry> findAllByStudent(Student student);
}
