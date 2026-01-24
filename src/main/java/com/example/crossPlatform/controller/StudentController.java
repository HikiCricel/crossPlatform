package com.example.crossPlatform.controller;

import java.util.ArrayList;
import java.util.List;

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

import com.example.crossPlatform.dto.DeadlinePrediction;
import com.example.crossPlatform.dto.DeadlineResponseDTO;
import com.example.crossPlatform.dto.StudentRequestDTO;
import com.example.crossPlatform.dto.StudentResponseDTO;
import com.example.crossPlatform.service.DeadlineService;
import com.example.crossPlatform.service.StudentService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;
    private final DeadlineService deadlineService;
    private static final Logger logger = LoggerFactory.getLogger(StudentController.class);

    @PostMapping
    public ResponseEntity<StudentResponseDTO> addStudent(@RequestBody @Valid StudentRequestDTO student) {
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

    // @GetMapping
    // public List<StudentResponseDTO> getStudents(@RequestParam(required = false)
    // String name) {
    // if (name == null)
    // return studentService.getAll();
    // else
    // return studentService.getAllByGroup(name);
    // }

    @GetMapping("/{id}")
    public ResponseEntity<StudentResponseDTO> getStudent(@PathVariable Long id) {
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

    @PatchMapping("/{id}")
    public ResponseEntity<Object> editStudent(@PathVariable Long id, @RequestBody StudentRequestDTO student) {
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

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
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

    @GetMapping("/filter")
    public ResponseEntity<Object> getByFilter(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String title,
            @PageableDefault(page = 0, size = 10, sort = "title") Pageable pageable) {
        logger.info("Received request to get Students with filter");
        try {
            return ResponseEntity.ok(studentService.getByFilter(name, title, pageable));
        } catch (Exception e) {
            logger.error("Error while getting filtered Students. Error: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PatchMapping("/{studentId}/deadlines")
    public ResponseEntity<Object> addDeadlineToStudent(@PathVariable Long studentId, @RequestParam Long deadlineId) {
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

    @PatchMapping("/{studentId}/deadlines/delete")
    public ResponseEntity<Object> removeDeadlineFromStudent(@PathVariable Long studentId,
            @RequestParam Long deadlineId) {
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

    @GetMapping("/{studentId}/predictions")
    public List<DeadlinePrediction> getPredictions(@PathVariable Long studentId) {
        logger.info("Received request to get predictions for Student: {}", studentId);
        try {
            return deadlineService.getDeadlinePredictionsForStudent(studentId);
        } catch (Exception e) {
            logger.error("Error while getting Student predictions. Error: {}", e.getMessage(), e);
            return new ArrayList<>();
        }
    }
}
