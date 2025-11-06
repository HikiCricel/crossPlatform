package com.example.crossPlatform.service;

import java.util.List;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.crossPlatform.model.Student;
import com.example.crossPlatform.repository.StudentRepository;
import com.example.crossPlatform.specifications.StudentSpecifications;

@Service
@Transactional(readOnly = true)
public class StudentService {
    private final StudentRepository studentRepository;
    
    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }


    @Cacheable(value = "student", key = "#root.methodName")
    public List<Student> getAll() {
        return studentRepository.findAll();
    }

    
    public List<Student> getAllByGroup(String group) {
        return studentRepository.findAllByGroup(group);
    }

    @CacheEvict(value = "student", allEntries = true)
    @Transactional
    public Student create(Student student) {
        return studentRepository.save(student);
    }

    @Cacheable(value = "student", key = "#id")
    public Student getById(Long id) {
        // for (Student student : students) {
        //     if (student.getId().equals(id)) {
        //         return studentRepository.findById(id).orElse(null);
        //     }
        // }
        // return null;
        return studentRepository.findById(id).orElse(null);
    }

    @Transactional
    @Caching(evict = {@CacheEvict(value = "student", allEntries = true), @CacheEvict(value = "student", key = "#id")})
    public Student update(Long id, Student student) {
        return studentRepository.findById(id).map(existingStudent -> {
            existingStudent.setName(student.getName());
            existingStudent.setGroup(student.getGroup());
            return studentRepository.save(existingStudent);
        }).orElse(null);
    }

    @Transactional
    @Caching(evict = {@CacheEvict(value = "student", allEntries = true), @CacheEvict(value = "student", key = "#id")})
    public boolean deleteById(Long id) {
        if (studentRepository.existsById(id)) {
            studentRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public Page<Student> getByFilter(String name, String title, Pageable pageable){
        return studentRepository.findAll(StudentSpecifications.filter(name, title), pageable);
    }
}
