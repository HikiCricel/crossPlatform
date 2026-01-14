package com.example.crossPlatform.mapper;

import java.util.ArrayList;
import java.util.HashSet;

import com.example.crossPlatform.dto.StudentRequestDTO;
import com.example.crossPlatform.dto.StudentResponseDTO;
import com.example.crossPlatform.model.Student;

public class StudentMapper {
    public static Student studentRequestToStudent(StudentRequestDTO request) {
        return new Student(null, request.name(), request.group(), new ArrayList<>(), new HashSet<>());
    }

    public static StudentResponseDTO studentToStudentResponseDTO(Student student) {
        return new StudentResponseDTO(student.getId(), student.getName(), student.getGroup(),
                student.getRecentEntries(), student.getDeadlines());
    }
}
