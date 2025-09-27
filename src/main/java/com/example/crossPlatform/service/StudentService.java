package com.example.crossPlatform.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.crossPlatform.model.Student;
import com.example.crossPlatform.repository.StudentRepository;

@Service
public class StudentService {
    private final StudentRepository studentRepository;
    
    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }


    public List<Student> getAll() {
        return studentRepository.findAll();
    }

    
    public List<Student> getAllByGroup(String group) {
        return studentRepository.findAllByGroup(group);
    }

    public Student create(Student student) {
        return studentRepository.save(student);
    }

    public Student getById(Long id) {
        // for (Student student : students) {
        //     if (student.getId().equals(id)) {
        //         return studentRepository.findById(id).orElse(null);
        //     }
        // }
        // return null;
        return studentRepository.findById(id).orElse(null);
    }

    public Student update(Long id, Student student) {
        return studentRepository.findById(id).map(existingStudent -> {
            existingStudent.setName(student.getName());
            existingStudent.setGroup(student.getGroup());
            return studentRepository.save(existingStudent);
        }).orElse(null);
    }

    public boolean deleteById(Long id) {
        if (studentRepository.existsById(id)) {
            studentRepository.deleteById(id);
            return true;
        }
        return false;
    }  
}
