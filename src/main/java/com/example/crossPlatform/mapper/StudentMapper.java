package com.example.crossPlatform.mapper;

import java.util.ArrayList;

import com.example.crossPlatform.dto.StudentRequestDTO;
import com.example.crossPlatform.dto.StudentResponceDTO;
import com.example.crossPlatform.model.Student;

public class StudentMapper {
    public static Student studentRequestToStudent(StudentRequestDTO request) {
        return new Student(null, request.name(), request.group(), new ArrayList<>());
    }

    public static StudentResponceDTO studentToStudentResponceDTO(Student student) {
        return new StudentResponceDTO(student.getId(), student.getName(), student.getGroup(),
                student.getRecentEntries());
    }
}
