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

import com.example.crossPlatform.dto.StudentRequestDTO;
import com.example.crossPlatform.dto.StudentResponceDTO;
import com.example.crossPlatform.service.StudentService;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping("/students")
    public ResponseEntity<StudentResponceDTO> addStudent(@RequestBody @Valid StudentRequestDTO student) {
        return ResponseEntity.status(HttpStatus.CREATED).body(studentService.create(student));
    }

    @GetMapping("/students")
    public List<StudentResponceDTO> getStudents(@RequestParam(required = false) String name) {
        if (name == null)
            return studentService.getAll();
        else
            return studentService.getAllByGroup(name);
    }

    @GetMapping("/students/{id}")
    public ResponseEntity<StudentResponceDTO> getStudent(@PathVariable Long id) {
        return ResponseEntity.ok().body(studentService.getById(id));
    }

    @PatchMapping("/students/{id}")
    public ResponseEntity<Object> editStudent(@PathVariable Long id, @RequestBody StudentRequestDTO student) {
        StudentResponceDTO updated = studentService.update(id, student);
        if (updated != null) {
            return ResponseEntity.ok(updated);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/students/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        if (studentService.deleteById(id)) {
            ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping("/students/filter")
    public ResponseEntity<Object> getByFilter(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String title,
            @PageableDefault(page = 0, size = 10, sort = "title") Pageable pageable) {
        return ResponseEntity.ok(studentService.getByFilter(name, title, pageable));
    }
}
