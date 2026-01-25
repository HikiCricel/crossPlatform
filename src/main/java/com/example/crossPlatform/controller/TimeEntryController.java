package com.example.crossPlatform.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
import com.example.crossPlatform.service.TimeEntryService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/timeEntries")
@RequiredArgsConstructor
@Tag(name = "TimeEntries", description = "Methods for managing timeEntries")
public class TimeEntryController {
    private final TimeEntryService timeEntryService;
    private static final Logger logger = LoggerFactory.getLogger(TimeEntryController.class);

    @Operation(summary = "Create New TimeEntry", description = "Creates a new timeEntry in the system")
    @PostMapping
    public ResponseEntity<TimeEntryResponseDTO> addTimEntry(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "TimeEntry data to create", required = true) @RequestBody @Valid TimeEntryRequestDTO timeEntry) {
        logger.info("Received request to add TimEntry");
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(timeEntryService.create(timeEntry));
        } catch (IllegalArgumentException e) {
            logger.warn("Invalid add TimEntry request: {}", e.getMessage());
            return ResponseEntity.badRequest().body(null);
        } catch (Exception e) {
            logger.error("Error while adding new TimEntry: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @Operation(summary = "Get TimeEntry by ID", description = "Retrieves a specific timeEntry by its unique Id")
    @GetMapping("/{id}")
    public ResponseEntity<TimeEntryResponseDTO> getTimeEntry(
            @Parameter(description = "ID of the timeEntry to retrieve", required = true) @PathVariable Long id) {
        logger.info("Received request to get TimeEntry with id: {}", id);
        try {
            return ResponseEntity.ok().body(timeEntryService.getById(id));
        } catch (IllegalArgumentException e) {
            logger.warn("Id value is invalid: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            logger.error("Error while getting TimeEntry with id: {}. Error: {}", id, e.getMessage(), e);
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Get All TimeEntries", description = "Retrieves a list of all timeEntries in the system by task type if it's not null, else retrieves a list of all timeEntries")
    @GetMapping("/type")
    public List<TimeEntryResponseDTO> getTimeEntriesByType(
            @Parameter(description = "Task type of the timeEntry", required = true) @RequestParam(required = false) TaskType type) {
        logger.info("Received request to get TimeEntry with TaskType: {}", type);
        if (type == null)
            return timeEntryService.getAll();
        else
            return timeEntryService.getAllByType(type);
    }

    @Operation(summary = "Get All TimeEntries", description = "Retrieves a list of all timeEntries in the system by studentId if it's not null, else retrieves a list of all timeEntries")
    @GetMapping("/studentId")
    public List<TimeEntryResponseDTO> getTimeEntriesByStudentId(
            @Parameter(description = "Id of the student", required = true) @RequestParam(required = false) Long studentId) {
        if (studentId == null)
            return timeEntryService.getAll();
        else
            return timeEntryService.getAllByStudentId(studentId);
    }

    @Operation(summary = "Update TimeEntry", description = "Updates an existing timeEntry")
    @PatchMapping("/{id}")
    public ResponseEntity<Object> editTimeEntry(
            @Parameter(description = "ID of the timeEntry to update", required = true) @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Updated timeEntry data", required = true) @RequestBody TimeEntryRequestDTO timeEntry) {
        logger.info("Received request to edit TimeEntry: {}", id);
        try {
            TimeEntryResponseDTO updated = timeEntryService.update(id, timeEntry);
            if (updated != null) {
                return ResponseEntity.ok(updated);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (IllegalArgumentException e) {
            logger.warn("Id value is invalid: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            logger.error("Error while updating TimeEntry: {}. Error: {}", id, e.getMessage(), e);
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Filter TimeEntry", description = "Search and filter timeEntries")
    @GetMapping("/filter")
    public ResponseEntity<Object> getByFilter(
            @Parameter(description = "Task type of the timeEntry to filter by") @RequestParam(required = false) TaskType type,
            @Parameter(description = "isBillable field of the timeEntry to filter by") @RequestParam(defaultValue = "false", required = false) boolean isBillable,
            @Parameter(description = "The field by which the result is sorted, it can be type, isBillable or id") @RequestParam(defaultValue = "type") String sort) {
        logger.info("Received request to get TimeEntries with filter");
        if (!List.of("type", "isBillable", "id").contains(sort)) {
            sort = "type";
        }
        Sort sortOrder = Sort.by(sort);
        Pageable pageable = PageRequest.of(0, 10, sortOrder);
        return ResponseEntity.ok(timeEntryService.getByFilter(type, isBillable, pageable));
    }

    @Operation(summary = "Delete TimeEntry", description = "Deletes a timeEntry from the system")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTimEntry(
            @Parameter(description = "ID of the timeEntry to delete", required = true) @PathVariable Long id) {
        logger.info("Received request to delete TimeEntry with id: {}", id);

        try {
            if (timeEntryService.deleteById(id)) {
                return ResponseEntity.noContent().build();
            }

            logger.warn("Can't delete TimeEntry with id: {}", id);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            logger.error("Error while deleting TimeEntry with id: {}. Error: {}", id, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @Operation(summary = "Set EndTime", description = "Sets the end date and time of a timeEntry")
    @PatchMapping("/{id}/endTime")
    public ResponseEntity<Object> setTimeEnd(
            @Parameter(description = "ID of the timeEntry to set endTime", required = true) @PathVariable long id) {
        logger.info("Received request to set end time for TimeEntry");
        try {
            TimeEntryResponseDTO updated = timeEntryService.setTimeEnd(id);
            if (updated != null) {
                return ResponseEntity.ok(updated);
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            logger.error("Error while setting end time for TimeEntry. Error: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
