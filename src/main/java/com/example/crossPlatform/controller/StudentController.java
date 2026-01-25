package com.example.crossPlatform.controller;

import java.util.ArrayList;
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

import com.example.crossPlatform.dto.DeadlinePrediction;
import com.example.crossPlatform.dto.DeadlineResponseDTO;
import com.example.crossPlatform.dto.StudentRequestDTO;
import com.example.crossPlatform.dto.StudentResponseDTO;
import com.example.crossPlatform.service.DeadlineService;
import com.example.crossPlatform.service.StudentService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
@Tag(name = "Students", description = "Methods for managing students")
public class StudentController {

    private final StudentService studentService;
    private final DeadlineService deadlineService;
    private static final Logger logger = LoggerFactory.getLogger(StudentController.class);

    @Operation(summary = "Create New Student", description = "Creates a new student in the system")
    @PostMapping
    public ResponseEntity<StudentResponseDTO> addStudent(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Student data to create", required = true) @RequestBody @Valid StudentRequestDTO student) {
        logger.info("Received request to add Student");
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(studentService.create(student));
        } catch (IllegalArgumentException e) {
            logger.warn("Invalid add Student request: {}", e.getMessage());
            return ResponseEntity.badRequest().body(null);
        } catch (Exception e) {
            logger.error("Error while adding new Student: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @Operation(summary = "Get All Students", description = "Retrieves a list of all students in the system by group if it's not null, else retrieves a list of all students")
    @GetMapping("/group")
    public List<StudentResponseDTO> getStudents(
            @Parameter(description = "Group of the student", required = true) @RequestParam(required = false) String group) {
        logger.info("Received request to get Students with group: {}", group);
        if (group == null)
            return studentService.getAll();
        else
            return studentService.getAllByGroup(group);
    }

    @Operation(summary = "Get Student by ID", description = "Retrieves a specific student by its unique Id")
    @GetMapping("/{id}")
    public ResponseEntity<StudentResponseDTO> getStudent(
            @Parameter(description = "ID of the student to retrieve", required = true) @PathVariable Long id) {
        logger.info("Received request to get Student with id: {}", id);
        try {
            return ResponseEntity.ok().body(studentService.getById(id));
        } catch (IllegalArgumentException e) {
            logger.warn("Id value is invalid: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            logger.error("Error while getting Student with id: {}. Error: {}", id, e.getMessage(), e);
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Update Student", description = "Updates an existing student")
    @PatchMapping("/{id}")
    public ResponseEntity<Object> editStudent(
            @Parameter(description = "ID of the student to update", required = true) @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Updated student data", required = true) @RequestBody StudentRequestDTO student) {
        logger.info("Received request to edit Student: {}", id);
        try {
            StudentResponseDTO updated = studentService.update(id, student);
            if (updated != null) {
                return ResponseEntity.ok(updated);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (IllegalArgumentException e) {
            logger.warn("Id value is invalid: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            logger.error("Error while updating Student: {}. Error: {}", id, e.getMessage(), e);
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Delete Student", description = "Deletes a student from the system")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(
            @Parameter(description = "ID of the student to delete", required = true) @PathVariable Long id) {
        logger.info("Received request to delete Student with id: {}", id);
        try {
            if (studentService.deleteById(id)) {
                return ResponseEntity.noContent().build();
            }
            logger.warn("Can't delete Student with id: {}", id);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            logger.error("Error while deleting Student with id: {}. Error: {}", id, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @Operation(summary = "Filter Student", description = "Search and filter students")
    @GetMapping("/filter")
    public ResponseEntity<Object> getByFilter(
            @Parameter(description = "Name of the student to filter by") @RequestParam(required = false) String name,
            @Parameter(description = "Group of the student to filter by") @RequestParam(required = false) String group,
            @Parameter(description = "The field by which the result is sorted, it can be name, group or id") @RequestParam(defaultValue = "name") String sort) {
        logger.info("Received request to get Students with filter");
        try {
            if (!List.of("name", "group", "id").contains(sort)) {
                sort = "name";
            }
            Sort sortOrder = Sort.by(sort);
            Pageable pageable = PageRequest.of(0, 10, sortOrder);
            return ResponseEntity.ok(studentService.getByFilter(name, group, pageable));
        } catch (Exception e) {
            logger.error("Error while getting filtered Students. Error: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @Operation(summary = "Apply deadline to student", description = "Applies a certain deadline to certain student")
    @PatchMapping("/{studentId}/deadlines")
    public ResponseEntity<Object> addDeadlineToStudent(
            @Parameter(description = "ID of the student to apply deadline", required = true) @PathVariable Long studentId,
            @Parameter(description = "ID of the deadline to aplly", required = true) @RequestParam Long deadlineId) {
        logger.info("Received request to add Deadline to Student");
        try {
            DeadlineResponseDTO updated = deadlineService.addDeadlineToStudent(studentId, deadlineId);
            if (updated != null)
                return ResponseEntity.ok(updated);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            logger.error("Error while adding Deadline to Student. Error: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @Operation(summary = "Detach deadline to student", description = "Detaches a certain deadline from certain student")
    @PatchMapping("/{studentId}/deadlines/delete")
    public ResponseEntity<Object> removeDeadlineFromStudent(
            @Parameter(description = "ID of the student from which the deadline is detached", required = true) @PathVariable Long studentId,
            @Parameter(description = "ID of the deadline to detach", required = true) @RequestParam Long deadlineId) {
        logger.info("Received request to remove Deadline from Student");
        try {
            DeadlineResponseDTO updated = deadlineService.removeDeadlineFromStudent(studentId, deadlineId);
            if (updated != null)
                return ResponseEntity.ok(updated);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            logger.error("Error while removing Deadline from Student. Error: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @Operation(summary = "Get Deadline Predicitons", description = "Gets deadline predictions for certain student")
    @GetMapping("/{studentId}/predictions")
    public List<DeadlinePrediction> getPredictions(
            @Parameter(description = "ID of the student for whom deadline are predicted", required = true) @PathVariable Long studentId) {
        logger.info("Received request to get predictions for Student: {}", studentId);
        try {
            return deadlineService.getDeadlinePredictionsForStudent(studentId);
        } catch (Exception e) {
            logger.error("Error while getting Student predictions. Error: {}", e.getMessage(), e);
            return new ArrayList<>();
        }
    }
}
