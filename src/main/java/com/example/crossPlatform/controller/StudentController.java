package com.example.crossPlatform.controller;

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

import com.example.crossPlatform.model.Student;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class StudentController {
    private List<Student> students = new ArrayList<>(Arrays.asList(
    new Student(1l, "Фисенко Д.Р.", "2231121", null), 
    new Student(2l, "Зубков Е.В.", "2201117", null)
    ));
    
    @GetMapping("/students")
    public List<Student> getStudents() {
        return students;
    }

    @GetMapping("/students/{id}")
    public ResponseEntity<Student> getStudent(@PathVariable Long id) {
        for (Student student : students) {
            if (student.getId().equals(id)) {
                return ResponseEntity.ok(student);
            }
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/students")
    public ResponseEntity<Student> addStudent(@RequestBody @Valid Student student) {
        student.setId((long)students.size() + 1);
        students.add(student);
        return ResponseEntity.status(HttpStatus.CREATED).body(student);
    }
}
