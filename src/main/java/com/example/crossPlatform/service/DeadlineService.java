package com.example.crossPlatform.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
import com.example.crossPlatform.specifications.DeadlineSpecifications;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class DeadlineService {
    private List<Deadline> deadlines = new ArrayList<>();
    private String entNotFndExcpt = "Student not found";

    private final Logger logger = LoggerFactory.getLogger(DeadlineService.class);
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
        logger.info("Successfully retrieved all Deadlines. Total count: {}", deadlinesResponse.size());
        return deadlinesResponse;
    }

    // public List<DeadlineResponseDTO> getAllByType(TaskType type) {
    //     deadlines = deadlineRepository.findAllByType(type);
    //     List<DeadlineResponseDTO> deadlinesResponse = new ArrayList<>();
    //     for (Deadline deadline : deadlines) {
    //         deadlinesResponse.add(DeadlineMapper.deadlineToDeadlineResponseDTO(deadline));
    //     }
    //     logger.info("Successfully retrieved all Deadlines by type. Total count: {}", deadlinesResponse.size());
    //     return deadlinesResponse;
    // }

    // public List<DeadlineResponseDTO> getAllByStudentId(Long id) {
    //     deadlines = deadlineRepository.findAllByStudentId(id);
    //     List<DeadlineResponseDTO> deadlinesResponse = new ArrayList<>();
    //     for (Deadline deadline : deadlines) {
    //         deadlinesResponse.add(DeadlineMapper.deadlineToDeadlineResponseDTO(deadline));
    //     }
    //     logger.info("Successfully retrieved all Deadlines by studentId. Total count: {}", deadlinesResponse.size());
    //     return deadlinesResponse;
    // }

    @CacheEvict(value = "deadline", allEntries = true)
    @Transactional
    public DeadlineResponseDTO create(DeadlineRequestDTO request) {
        logger.info("Creating new Deadline: {} with type: {}", request.subject(), request.type());
        Deadline deadline = deadlineRepository.save(DeadlineMapper.deadlineRequestToDeadline(request));
        logger.info("Successfully created Deadline with ID: {} with type: {}", deadline.getId(), deadline.getType());
        return DeadlineMapper.deadlineToDeadlineResponseDTO(deadline);
    }

    @Cacheable(value = "deadline", key = "#id")
    public DeadlineResponseDTO getById(Long id) {
        Deadline deadline = deadlineRepository.findById(id).orElse(null);
        if (deadline != null) {
            logger.info("Successfully retrieved Deadline by id: {}.", deadline.getId());
            return DeadlineMapper.deadlineToDeadlineResponseDTO(deadline);
        }
        throw new EntityNotFoundException("There's no Deadline with ID: " + id.toString());
    }

    @Transactional
    @Caching(evict = { @CacheEvict(value = "deadlines", allEntries = true),
            @CacheEvict(value = "deadline", key = "#id") })
    public DeadlineResponseDTO update(Long id, DeadlineRequestDTO request) {
        Deadline deadline = deadlineRepository.findById(id).map(existingDeadline -> {
            logger.debug("Values before update - subject: {}, type: {}", existingDeadline.getSubject(),
                    existingDeadline.getType());
            existingDeadline.setSubject(request.subject());
            existingDeadline.setDeadlineDate(request.deadline());
            logger.info("Successfully updated Deadline with ID: {}", id);
            return deadlineRepository.save(existingDeadline);
        }).orElse(null);
        if (deadline == null) {
            throw new EntityNotFoundException("There's no Deadline with ID: " + id.toString());
        }
        return DeadlineMapper.deadlineToDeadlineResponseDTO(deadline);
    }

    @Transactional
    @Caching(evict = { @CacheEvict(value = "deadlines", allEntries = true),
            @CacheEvict(value = "deadline", key = "#id") })
    public boolean deleteById(Long id) {
        if (deadlineRepository.existsById(id)) {
            deadlineRepository.deleteById(id);
            logger.info("Successfully deleted Deadline with ID: {}", id);
            return true;
        }
        throw new EntityNotFoundException("There's no Deadline with ID: " + id.toString());
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
        logger.info("Successfully added deadline ID: {} to student ID: {}", deadlineId, studentId);
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
        logger.info("Successfully removed deadline ID: {} from student ID: {}", deadlineId, studentId);
        return DeadlineMapper.deadlineToDeadlineResponseDTO(deadline);
    }

    @Transactional
    public List<DeadlinePrediction> getDeadlinePredictionsForStudent(Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new EntityNotFoundException(entNotFndExcpt));

        logger.info("Predictions successfully identified for student ID: {}", studentId);
        return student.getDeadlines().stream()
                .map(deadline -> deadlineMapper.deadlineToDeadlinePredictionDto(deadline, studentId)).toList();
    }

    public Page<Deadline> getByFilter(TaskType type, Long id, Pageable pageable) {
        Page<Deadline> result = deadlineRepository.findAll(DeadlineSpecifications.filter(type, id), pageable);
        logger.info("Successfully filtered Deadlines. Found {} results", result.getNumberOfElements());
        return result;
    }
}