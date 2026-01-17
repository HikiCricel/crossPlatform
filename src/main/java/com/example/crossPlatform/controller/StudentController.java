package com.example.crossPlatform.controller;

import java.util.List;

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
import org.springframework.web.bind.annotation.RestController;

import com.example.crossPlatform.dto.DeadlinePrediction;
import com.example.crossPlatform.dto.DeadlineResponseDTO;
import com.example.crossPlatform.dto.StudentRequestDTO;
import com.example.crossPlatform.dto.StudentResponseDTO;
import com.example.crossPlatform.service.DeadlineService;
import com.example.crossPlatform.service.StudentService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;
    private final DeadlineService deadlineService;

    @PostMapping
    public ResponseEntity<StudentResponseDTO> addStudent(@RequestBody @Valid StudentRequestDTO student) {
        return ResponseEntity.status(HttpStatus.CREATED).body(studentService.create(student));
    }

    @GetMapping
    public List<StudentResponseDTO> getStudents(@RequestParam(required = false) String name) {
        if (name == null)
            return studentService.getAll();
        else
            return studentService.getAllByGroup(name);
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudentResponseDTO> getStudent(@PathVariable Long id) {
        return ResponseEntity.ok().body(studentService.getById(id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> editStudent(@PathVariable Long id, @RequestBody StudentRequestDTO student) {
        StudentResponseDTO updated = studentService.update(id, student);
        if (updated != null) {
            return ResponseEntity.ok(updated);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        if (studentService.deleteById(id)) {
            ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping("/filter")
    public ResponseEntity<Object> getByFilter(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String title,
            @PageableDefault(page = 0, size = 10, sort = "title") Pageable pageable) {
        return ResponseEntity.ok(studentService.getByFilter(name, title, pageable));
    }

    @PatchMapping("/{studentId}/deadlines")
    public ResponseEntity<Object> addDeadlineToStudent(@PathVariable Long studentId, @RequestParam Long deadlineId) {
        DeadlineResponseDTO updated = deadlineService.addDeadlineToStudent(studentId, deadlineId);
        if (updated != null)
            return ResponseEntity.ok(updated);
        return ResponseEntity.notFound().build();
    }

    @PatchMapping("/{studentId}/deadlines/delete")
    public ResponseEntity<Object> removeDeadlineFromStudent(@PathVariable Long studentId,
            @RequestParam Long deadlineId) {
        DeadlineResponseDTO updated = deadlineService.removeDeadlineFromStudent(studentId, deadlineId);
        if (updated != null)
            return ResponseEntity.ok(updated);
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{studentId}/predictions")
    public List<DeadlinePrediction> getPredictions(@PathVariable Long studentId) {
        return deadlineService.getDeadlinePredictionsForStudent(studentId);
    }
}
