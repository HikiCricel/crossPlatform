package com.example.crossPlatform.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.example.crossPlatform.service.TimeEntryService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/timeEntries")
@RequiredArgsConstructor
public class TimeEntryController {
    private final TimeEntryService timeEntryService;
    private static final Logger logger = LoggerFactory.getLogger(TimeEntryController.class);

    @PostMapping
    public ResponseEntity<TimeEntryResponseDTO> addTimEntry(@RequestBody @Valid TimeEntryRequestDTO timeEntry) {
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

    @GetMapping("/{id}")
    public ResponseEntity<TimeEntryResponseDTO> getTimeEntry(@PathVariable Long id) {
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

    // @GetMapping
    // public List<TimeEntryResponseDTO> getTimeEntriesByType(@RequestParam(required
    // = false) TaskType timeEntry) {
    // if (timeEntry == null)
    // return timeEntryService.getAll();
    // else
    // return timeEntryService.getAllByType(timeEntry);
    // }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> editTimeEntry(@PathVariable Long id, @RequestBody TimeEntryRequestDTO timeEntry) {
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

    @GetMapping("/filter")
    public ResponseEntity<Object> getByFilter(
            @RequestParam(required = false) Long studentId,
            @RequestParam(required = false) TaskType type,
            @RequestParam(required = false) boolean expression,
            @PageableDefault(page = 0, size = 10, sort = "title") Pageable pageable) {
        logger.info("Received request to get TimeEntries with filter");
        try {
            return ResponseEntity.ok(timeEntryService.getByFilter(type, studentId, expression, pageable));
        } catch (Exception e) {
            logger.error("Error while getting filtered TimeEntries. Error: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTimEntry(@PathVariable Long id) {
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

    @PatchMapping("/{id}/endTime")
    public ResponseEntity<Object> setTimeEnd(@PathVariable long id) {
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
