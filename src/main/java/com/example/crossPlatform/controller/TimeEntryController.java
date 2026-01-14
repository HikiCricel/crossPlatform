package com.example.crossPlatform.controller;

import java.time.LocalDateTime;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.crossPlatform.dto.TimeEntryRequestDTO;
import com.example.crossPlatform.dto.TimeEntryResponseDTO;
import com.example.crossPlatform.enums.TaskType;
import com.example.crossPlatform.model.Student;
import com.example.crossPlatform.service.TimeEntryService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class TimeEntryController {
    private final TimeEntryService timeEntryService;

    public TimeEntryController(TimeEntryService timeEntryService) {
        this.timeEntryService = timeEntryService;
    }

    @PostMapping("/timeEntries")
    public ResponseEntity<TimeEntryResponseDTO> addTimEntity(@RequestBody @Valid TimeEntryRequestDTO timeEntry) {
        return ResponseEntity.status(HttpStatus.CREATED).body(timeEntryService.create(timeEntry));
    }

    @GetMapping("/timeEntries/{id}")
    public ResponseEntity<TimeEntryResponseDTO> getTimeEntry(@PathVariable Long id) {
        return ResponseEntity.ok().body(timeEntryService.getById(id));
    }

    @PatchMapping("/timeEntries/{id}")
    public ResponseEntity<Object> editStudent(@PathVariable Long id, @RequestBody TimeEntryRequestDTO timeEntry) {
        TimeEntryResponseDTO updated = timeEntryService.update(id, timeEntry);
        if (updated != null) {
            return ResponseEntity.ok(updated);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/timeEntries/filter")
    public ResponseEntity<Object> getByFilter(
            @RequestParam(required = false) Student student,
            @RequestParam(required = false) TaskType type,
            @RequestParam(required = false) LocalDateTime dateTimeStart,
            @RequestParam(required = false) LocalDateTime dateTimeEnd,
            @RequestParam(required = false) boolean expression,
            @PageableDefault(page = 0, size = 10, sort = "title") Pageable pageable) {
        return ResponseEntity
                .ok(timeEntryService.getByFilter(student, type, dateTimeStart, dateTimeEnd, expression, pageable));
    }

    @DeleteMapping("/timeEntries/{id}")
    public ResponseEntity<Void> deleteTimEntity(@PathVariable Long id) {
        if (timeEntryService.deleteById(id)) {
            ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok().build();
    }
}
