package com.example.crossPlatform.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.crossPlatform.dto.DeadlinePrediction;
import com.example.crossPlatform.dto.DeadlineRequestDTO;
import com.example.crossPlatform.dto.DeadlineResponseDTO;
import com.example.crossPlatform.enums.TaskType;
import com.example.crossPlatform.mapper.DeadlineMapper;
import com.example.crossPlatform.model.Deadline;
import com.example.crossPlatform.model.Student;
import com.example.crossPlatform.repository.DeadlineRepository;
import com.example.crossPlatform.repository.StudentRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class DeadlineService {
    private List<Deadline> deadlines = new ArrayList<>();
    private String entNotFndExcpt = "Student not found";

    private final DeadlineRepository deadlineRepository;
    private final StudentRepository studentRepository;
    private final DeadlineMapper deadlineMapper;

    @Cacheable(value = "deadlines", key = "#root.methodName")
    public List<DeadlineResponseDTO> getAll() {
        deadlines = deadlineRepository.findAll();
        List<DeadlineResponseDTO> deadlinesResponse = new ArrayList<>();
        for (Deadline deadline : deadlines) {
            deadlinesResponse.add(DeadlineMapper.deadlineToDeadlineResponseDTO(deadline));
        }
        return deadlinesResponse;
    }

    public List<DeadlineResponseDTO> getAllByType(TaskType type) {
        deadlines = deadlineRepository.findAllByType(type);
        List<DeadlineResponseDTO> deadlinesResponse = new ArrayList<>();
        for (Deadline deadline : deadlines) {
            deadlinesResponse.add(DeadlineMapper.deadlineToDeadlineResponseDTO(deadline));
        }
        return deadlinesResponse;
    }

    public List<DeadlineResponseDTO> getAllByStudentId(Long id) {
        deadlines = deadlineRepository.findAllByStudentId(id);
        List<DeadlineResponseDTO> deadlinesResponse = new ArrayList<>();
        for (Deadline deadline : deadlines) {
            deadlinesResponse.add(DeadlineMapper.deadlineToDeadlineResponseDTO(deadline));
        }
        return deadlinesResponse;
    }

    @CacheEvict(value = "deadline", allEntries = true)
    @Transactional
    public DeadlineResponseDTO create(DeadlineRequestDTO request) {
        Deadline deadline = deadlineRepository.save(DeadlineMapper.deadlineRequestToDeadline(request));
        return DeadlineMapper.deadlineToDeadlineResponseDTO(deadline);
    }

    @Cacheable(value = "deadline", key = "#id")
    public DeadlineResponseDTO getById(Long id) {
        Deadline deadline = deadlineRepository.findById(id).orElse(null);
        return DeadlineMapper.deadlineToDeadlineResponseDTO(deadline);
    }

    @Transactional
    @Caching(evict = { @CacheEvict(value = "deadlines", allEntries = true),
            @CacheEvict(value = "deadline", key = "#id") })
    public DeadlineResponseDTO update(Long id, DeadlineRequestDTO request) {
        Deadline deadline = deadlineRepository.findById(id).map(existingDeadline -> {
            existingDeadline.setSubject(request.subject());
            existingDeadline.setDeadlineDate(request.deadline());
            return deadlineRepository.save(existingDeadline);
        }).orElse(null);
        return DeadlineMapper.deadlineToDeadlineResponseDTO(deadline);
    }

    @Transactional
    @Caching(evict = { @CacheEvict(value = "deadlines", allEntries = true),
            @CacheEvict(value = "deadline", key = "#id") })
    public boolean deleteById(Long id) {
        if (deadlineRepository.existsById(id)) {
            deadlineRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Transactional
    public DeadlineResponseDTO addDeadlineToStudent(Long studentId, Long deadlineId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new EntityNotFoundException(entNotFndExcpt));
        Deadline deadline = deadlineRepository.findById(deadlineId)
                .orElseThrow(() -> new EntityNotFoundException("Deadline not found"));
        student.getDeadlines().add(deadline);
        deadline.getStudents().add(student);
        studentRepository.save(student);
        deadlineRepository.save(deadline);
        return DeadlineMapper.deadlineToDeadlineResponseDTO(deadline);
    }

    @Transactional
    public DeadlineResponseDTO removeDeadlineFromStudent(Long studentId, Long deadlineId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new EntityNotFoundException(entNotFndExcpt));
        Deadline deadline = deadlineRepository.findById(deadlineId)
                .orElseThrow(() -> new EntityNotFoundException("Deadline not found"));

        student.getDeadlines().remove(deadline);
        deadline.getStudents().remove(student);
        studentRepository.save(student);
        deadlineRepository.save(deadline);
        return DeadlineMapper.deadlineToDeadlineResponseDTO(deadline);
    }

    @Transactional
    public List<DeadlinePrediction> getDeadlinePredictionsForStudent(Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new EntityNotFoundException(entNotFndExcpt));

        return student.getDeadlines().stream()
                .map(deadline -> deadlineMapper.deadlineToDeadlinePredictionDto(deadline, studentId)).toList();
    }
}