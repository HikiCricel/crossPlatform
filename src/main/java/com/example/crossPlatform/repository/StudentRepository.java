package com.example.crossPlatform.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.example.crossPlatform.model.Student;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long>, JpaSpecificationExecutor<Student> {
        List<Student> findByGroupStartingWithIgnoreCase(String group);

        List<Student> findAllByGroup(String group);
}
