package com.example.crossPlatform.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.crossPlatform.dto.TimeEntryRequestDTO;
import com.example.crossPlatform.dto.TimeEntryResponseDTO;
import com.example.crossPlatform.enums.TaskType;
import com.example.crossPlatform.mapper.TimeEntryMapper;
import com.example.crossPlatform.model.Student;
import com.example.crossPlatform.model.TimeEntry;
import com.example.crossPlatform.repository.StudentRepository;
import com.example.crossPlatform.repository.TimeEntryRepository;
import com.example.crossPlatform.specifications.TimeEntrySpecifications;

import jakarta.persistence.EntityNotFoundException;

@Service
@Transactional(readOnly = true)
public class TimeEntryService {
    private final TimeEntryRepository timeEntryRepository;
    private final StudentRepository studentRepository;

    public TimeEntryService(TimeEntryRepository timeEntryRepository, StudentRepository studentRepository) {
        this.timeEntryRepository = timeEntryRepository;
        this.studentRepository = studentRepository;
    }

    @Cacheable(value = "timeEntries", key = "#root.methodName")
    public List<TimeEntry> getAll() {
        return timeEntryRepository.findAll();
    }

    public List<TimeEntry> getAllByDescription(String description) {
        return timeEntryRepository.findAllByDescription(description);
    }

    public List<TimeEntry> getAllByType(TaskType type) {
        return timeEntryRepository.findAllByType(type);
    }

    public List<TimeEntry> getAllByStudent(Student student) {
        return timeEntryRepository.findAllByStudent(student);
    }

    @Transactional
    @CacheEvict(value = "timeEntry", allEntries = true)
    public TimeEntryResponseDTO create(TimeEntryRequestDTO request) {
        Student student = studentRepository.findById(request.studentId())
                .orElseThrow(() -> new EntityNotFoundException("Student not found with id: " + request.studentId()));
        TimeEntry timeEntry = new TimeEntry();
        timeEntry.setStudent(student);
        timeEntry.setType(request.type());
        timeEntry.setSubject(request.subject());
        timeEntry.setTimeStart(LocalDateTime.now());
        timeEntry.setTimeEnd(null);
        timeEntry.setDuration(0.0);
        timeEntry.setDescription("Auto-created entry");
        timeEntry.setBillable(false);

        TimeEntry saved = timeEntryRepository.save(timeEntry);
        return TimeEntryMapper.timeEntryToTimeEntryResponseDTO(saved);
        // TimeEntry timeEntry = timeEntryRepository.save(TimeEntryMapper.timeEntryRequestToTimeEntry(request));
        // return TimeEntryMapper.timeEntryToTimeEntryResponceDTO(timeEntry);
    }

    @Cacheable(value = "timeEntry", key = "#id")
    public TimeEntryResponseDTO getById(Long id) {
        TimeEntry timeEntry = timeEntryRepository.findById(id).orElse(null);
        return TimeEntryMapper.timeEntryToTimeEntryResponseDTO(timeEntry);
    }

    @Transactional
    @Caching(evict = { @CacheEvict(value = "timeEntries", allEntries = true),
            @CacheEvict(value = "timeEntry", key = "#id") })
    public TimeEntryResponseDTO update(Long id, TimeEntryRequestDTO request) {
        TimeEntry timeEntry = timeEntryRepository.findById(id).map(existingTimeEntry -> {
            // existingTimeEntry.setTimeStart(request.getTimeStart());
            // existingTimeEntry.setTimeEnd(request.getTimeEnd());
            return timeEntryRepository.save(existingTimeEntry);
        }).orElse(null);
        return TimeEntryMapper.timeEntryToTimeEntryResponseDTO(timeEntry);
    }

    @Transactional
    @Caching(evict = { @CacheEvict(value = "timeEntries", allEntries = true),
            @CacheEvict(value = "timeEntry", key = "#id") })
    public boolean deleteById(Long id) {
        if (timeEntryRepository.existsById(id)) {
            timeEntryRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public Page<TimeEntry> getByFilter(Student student, TaskType type, LocalDateTime dateTimeStart,
            LocalDateTime dateTimeEnd, boolean expression, Pageable pageable) {
        return timeEntryRepository.findAll(
                TimeEntrySpecifications.filter(type, student, dateTimeStart, dateTimeEnd, expression), pageable);
    }

}
