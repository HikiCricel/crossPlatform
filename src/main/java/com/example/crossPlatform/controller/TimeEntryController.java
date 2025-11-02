package com.example.crossPlatform.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.crossPlatform.enums.TaskType;
import com.example.crossPlatform.model.Student;
import com.example.crossPlatform.model.TimeEntry;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/timeEntries")
public class TimeEntryController {
    private List<TimeEntry> timeEntries = new ArrayList<>(Arrays.asList(
    new TimeEntry(1l, new Student(1l, "Фисенко Д.Р.", "2231121", null), TaskType.CODING , "Кусовая работа", LocalDateTime.now(), LocalDateTime.now(), false),
    new TimeEntry(2l, new Student(2l, "Зубков Е.В.", "2201117", null), TaskType.EXAM_PREP, "Экзамен", LocalDateTime.now(), LocalDateTime.now(), true)
    ));

    @GetMapping
    public List<TimeEntry> getTimeEntries() {
        return timeEntries;
    }

    @GetMapping("/timeEntries/{id}")
    public ResponseEntity<TimeEntry> getTimeEntry(@PathVariable Long id) {
        for (TimeEntry timeEntry : timeEntries) {
            if (timeEntry.getId().equals(id)) {
                return ResponseEntity.ok(timeEntry);
            }
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/timeEntries")
    public ResponseEntity<TimeEntry> addTimeEntry(@RequestBody @Valid TimeEntry timeEntry) {
        timeEntry.setId((long)timeEntries.size() + 1);
        timeEntries.add(timeEntry);
        return ResponseEntity.status(HttpStatus.CREATED).body(timeEntry);
    }
}

