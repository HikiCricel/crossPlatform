package com.example.crossPlatform.controller;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
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

import com.example.crossPlatform.dto.DeadlineRequestDTO;
import com.example.crossPlatform.dto.DeadlineResponseDTO;
import com.example.crossPlatform.enums.TaskType;
import com.example.crossPlatform.service.DeadlineService;
import com.example.crossPlatform.service.ReportService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/deadlines")
@RequiredArgsConstructor
@Tag(name = "Deadlines", description = "Methods for managing deadlines")
public class DeadlineController {
    private final DeadlineService deadlineService;
    private final ReportService reportService;
    private static final Logger logger = LoggerFactory.getLogger(DeadlineController.class);

    @Operation(summary = "Create New Deadline", description = "Creates a new deadline in the system")
    @PostMapping
    public ResponseEntity<DeadlineResponseDTO> addDeadline(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Deadline data to create", required = true) @RequestBody @Valid DeadlineRequestDTO deadline) {
        logger.info("Received request to add Deadline");
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(deadlineService.create(deadline));
        } catch (IllegalArgumentException e) {
            logger.warn("Invalid add Deadline request: {}", e.getMessage());
            return ResponseEntity.badRequest().body(null);
        } catch (Exception e) {
            logger.error("Error while adding new Deadline: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @Operation(summary = "Get All Deadlines", description = "Retrieves a list of all deadlines in the system by type if it's not null, else retrieves a list of all dealines")
    @GetMapping("/type")
    public List<DeadlineResponseDTO> getDeadlinesByType(
            @Parameter(description = "Task type to retrieve deadline", required = true) @RequestParam(required = false) TaskType type) {
        logger.info("Received request to get Deadlines with Tasktype: {}", type);
        try {
            if (type == null)
                return deadlineService.getAll();
            else
                return deadlineService.getAllByType(type);
        } catch (Exception e) {
            logger.error("Error while getting Deadlines: {}", e.getMessage(), e);
            return new ArrayList<>();
        }
    }

    @Operation(summary = "Get Deadline by studentID", description = "Retrieves a specific deadline by studentId that applied to it")
    @GetMapping("/studentId")
    public List<DeadlineResponseDTO> getDeadlinesByID(
            @Parameter(description = "ID of the student to retrieve deadline", required = true) @RequestParam(required = false) Long id) {
        logger.info("Received request to get Deadlines with ID: {}", id);
        try {
            if (id == null)
                return deadlineService.getAll();
            else
                return deadlineService.getAllByStudentId(id);
        } catch (Exception e) {
            logger.error("Error while getting Deadlines: {}", e.getMessage(), e);
            return new ArrayList<>();
        }
    }

    @Operation(summary = "Get Deadline by ID", description = "Retrieves a specific deadline by its unique Id")
    @GetMapping("/{id}")
    public ResponseEntity<DeadlineResponseDTO> getDeadline(
            @Parameter(description = "ID of the deadline to retrieve", required = true) @PathVariable Long id) {
        logger.info("Received request to get Deadline with id: {}", id);
        try {
            return ResponseEntity.ok().body(deadlineService.getById(id));
        } catch (IllegalArgumentException e) {
            logger.warn("Id value is invalid: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            logger.error("Error while getting Deadline with id: {}. Error: {}", id, e.getMessage(), e);
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Get Deadline Report", description = "Creates a report with all deadlines for a certain student")
    @GetMapping(value = "/getReport")
    public ResponseEntity<Resource> downloadDeadlinesForStudent(
            @Parameter(description = "ID of the student to create a report", required = true) @RequestParam(required = true) Long studentId) {
        Resource resource = reportService.getStudentDeadlineReport(studentId);
        return ResponseEntity.ok()
                .contentType(org.springframework.http.MediaType
                        .parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=deadlines_report.xlsx")
                .body(resource);
    }

    @Operation(summary = "Update Deadline", description = "Updates an existing deadline")
    @PatchMapping("/{id}")
    public ResponseEntity<Object> editStudent(
            @Parameter(description = "ID of the deadline to update", required = true) @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Updated deadline data", required = true) @RequestBody DeadlineRequestDTO deadline) {
        logger.info("Received request to edit Student with deadline: {}", id);
        try {
            DeadlineResponseDTO updated = deadlineService.update(id, deadline);
            if (updated != null) {
                return ResponseEntity.ok(updated);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (IllegalArgumentException e) {
            logger.warn("Id value is invalid: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            logger.error("Error while updating Student with deadline: {}. Error: {}", id, e.getMessage(), e);
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Delete Deadline", description = "Deletes a dealine from the system")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDeadline(
            @Parameter(description = "ID of the deadline to delete", required = true) @PathVariable Long id) {
        logger.info("Received request to delete Deadline with id: {}", id);

        try {
            if (deadlineService.deleteById(id)) {
                return ResponseEntity.noContent().build();
            }

            logger.warn("Can't delete Deadline with id: {}", id);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            logger.error("Error while deleting Deadline with id: {}. Error: {}", id, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
