package com.example.crossPlatform.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.crossPlatform.model.Student;

public interface StudentRepository extends JpaRepository<Student, Long> {
        List<Student> findByGroupStartingWithIgnoreCase(String group);

        List<Student> findAllByGroup(String group);
}
