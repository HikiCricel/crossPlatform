package com.example.crossPlatform.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.crossPlatform.dto.StudentRequestDTO;
import com.example.crossPlatform.dto.StudentResponseDTO;
import com.example.crossPlatform.mapper.StudentMapper;
import com.example.crossPlatform.model.Student;
import com.example.crossPlatform.repository.StudentRepository;
import com.example.crossPlatform.specifications.StudentSpecifications;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StudentService {
    private final StudentRepository studentRepository;
    private final Logger logger = LoggerFactory.getLogger(StudentService.class);

    @Cacheable(value = "student", key = "#root.methodName")
    public List<StudentResponseDTO> getAll() {
        List<Student> students = studentRepository.findAll();
        List<StudentResponseDTO> studentsResponse = new ArrayList<>();
        for (Student student : students) {
            studentsResponse.add(StudentMapper.studentToStudentResponseDTO(student));
        }
        logger.info("Successfully retrieved all Students. Total count: {}", studentsResponse.size());
        return studentsResponse;
    }

    public List<StudentResponseDTO> getAllByGroup(String group) {
        List<Student> students = studentRepository.findAllByGroup(group);
        List<StudentResponseDTO> studentsResponse = new ArrayList<>();
        for (Student student : students) {
            studentsResponse.add(StudentMapper.studentToStudentResponseDTO(student));
        }
        logger.info("Successfully retrieved all Students by group. Total count: {}", studentsResponse.size());
        return studentsResponse;
    }

    @CacheEvict(value = "student", allEntries = true)
    @Transactional
    public StudentResponseDTO create(StudentRequestDTO request) {
        logger.info("Creating new Student: {} group: {}", request.name(), request.group());
        Student student = studentRepository.save(StudentMapper.studentRequestToStudent(request));
        logger.info("Successfully created Student with ID: {} name: {}", student.getId(), student.getName());
        return StudentMapper.studentToStudentResponseDTO(student);
    }

    @Cacheable(value = "student", key = "#id")
    public StudentResponseDTO getById(Long id) {
        Student student = studentRepository.findById(id).orElse(null);
        if (student != null) {
            logger.info("Successfully retrieved Student by id: {}.", student.getId());
            return StudentMapper.studentToStudentResponseDTO(student);
        }
        throw new EntityNotFoundException("There's no Student with ID: " + id.toString());
    }

    @Transactional
    @Caching(evict = { @CacheEvict(value = "student", allEntries = true), @CacheEvict(value = "student", key = "#id") })
    public StudentResponseDTO update(Long id, StudentRequestDTO request) {
        Student student = studentRepository.findById(id).map(existingStudent -> {
            logger.debug("Values before update - name: {}, group: {}", existingStudent.getName(),
                    existingStudent.getGroup());
            existingStudent.setName(request.name());
            existingStudent.setGroup(request.group());
            logger.info("Successfully updated Student with ID: {}", id);
            return studentRepository.save(existingStudent);
        }).orElse(null);
        if (student == null) {
            throw new EntityNotFoundException("There's no Student with ID: " + id.toString());
        }
        return StudentMapper.studentToStudentResponseDTO(student);
    }

    @Transactional
    @Caching(evict = { @CacheEvict(value = "student", allEntries = true), @CacheEvict(value = "student", key = "#id") })
    public boolean deleteById(Long id) {
        if (studentRepository.existsById(id)) {
            studentRepository.deleteById(id);
            logger.info("Successfully deleted Student with ID: {}", id);
            return true;
        }
        throw new EntityNotFoundException("There's no Student with ID: " + id.toString());
    }

    public Page<StudentResponseDTO> getByFilter(String name, String group, Pageable pageable) {
        Page<Student> studentPage = studentRepository.findAll(StudentSpecifications.filter(name, group), pageable);

        List<StudentResponseDTO> dtoList = studentPage.getContent().stream()
                .map(StudentMapper::studentToStudentResponseDTO).toList();

        Page<StudentResponseDTO> result = new PageImpl<>(dtoList, pageable, studentPage.getTotalElements());

        logger.info("Successfully filtered Students. Found {} results", result.getNumberOfElements());
        return result;
    }
}
