package com.example.crossPlatform.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.crossPlatform.dto.StudentRequestDTO;
import com.example.crossPlatform.dto.StudentResponseDTO;
import com.example.crossPlatform.mapper.StudentMapper;
import com.example.crossPlatform.model.Student;
import com.example.crossPlatform.repository.StudentRepository;
import com.example.crossPlatform.specifications.StudentSpecifications;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StudentService {
    private final StudentRepository studentRepository;

    @Cacheable(value = "student", key = "#root.methodName")
    public List<StudentResponseDTO> getAll() {
        List<Student> students = studentRepository.findAll();
        List<StudentResponseDTO> studentsResponce = new ArrayList<>();
        for (Student student : students) {
            studentsResponce.add(StudentMapper.studentToStudentResponseDTO(student));
        }
        return studentsResponce;
    }

    public List<StudentResponseDTO> getAllByGroup(String group) {
        List<Student> students = studentRepository.findAllByGroup(group);
        List<StudentResponseDTO> studentsResponce = new ArrayList<>();
        for (Student student : students) {
            studentsResponce.add(StudentMapper.studentToStudentResponseDTO(student));
        }
        return studentsResponce;
    }

    @CacheEvict(value = "student", allEntries = true)
    @Transactional
    public StudentResponseDTO create(StudentRequestDTO request) {
        Student student = studentRepository.save(StudentMapper.studentRequestToStudent(request));
        return StudentMapper.studentToStudentResponseDTO(student);
    }

    @Cacheable(value = "student", key = "#id")
    public StudentResponseDTO getById(Long id) {
        Student student = studentRepository.findById(id).orElse(null);
        return StudentMapper.studentToStudentResponseDTO(student);
    }

    @Transactional
    @Caching(evict = { @CacheEvict(value = "student", allEntries = true), @CacheEvict(value = "student", key = "#id") })
    public StudentResponseDTO update(Long id, StudentRequestDTO request) {
        Student student = studentRepository.findById(id).map(existingStudent -> {
            existingStudent.setName(request.name());
            existingStudent.setGroup(request.group());
            return studentRepository.save(existingStudent);
        }).orElse(null);
        return StudentMapper.studentToStudentResponseDTO(student);
    }

    @Transactional
    @Caching(evict = { @CacheEvict(value = "student", allEntries = true), @CacheEvict(value = "student", key = "#id") })
    public boolean deleteById(Long id) {
        if (studentRepository.existsById(id)) {
            studentRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public Page<Student> getByFilter(String name, String group, Pageable pageable) {
        return studentRepository.findAll(StudentSpecifications.filter(name, group), pageable);
    }
}
