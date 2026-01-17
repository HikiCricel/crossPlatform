package com.example.crossPlatform.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TimeEntryService {
    private List<TimeEntry> timeEntries = new ArrayList<>();
    private final TimeEntryRepository timeEntryRepository;
    private final StudentRepository studentRepository;

    @Cacheable(value = "timeEntries", key = "#root.methodName")
    public List<TimeEntryResponseDTO> getAll() {
        timeEntries = timeEntryRepository.findAll();
        List<TimeEntryResponseDTO> timeEntriesResponse = new ArrayList<>();
        for (TimeEntry timeEntry : timeEntries) {
            timeEntriesResponse.add(TimeEntryMapper.timeEntryToTimeEntryResponseDTO(timeEntry));
        }
        return timeEntriesResponse;
    }

    public List<TimeEntryResponseDTO> getAllByType(TaskType type) {
        timeEntries = timeEntryRepository.findAllByType(type);
        List<TimeEntryResponseDTO> timeEntriesResponse = new ArrayList<>();
        for (TimeEntry timeEntry : timeEntries) {
            timeEntriesResponse.add(TimeEntryMapper.timeEntryToTimeEntryResponseDTO(timeEntry));
        }
        return timeEntriesResponse;
    }

    public List<TimeEntryResponseDTO> getAllByStudentId(Long id) {
        timeEntries = timeEntryRepository.findAllByStudentId(id);
        List<TimeEntryResponseDTO> timeEntriesResponse = new ArrayList<>();
        for (TimeEntry timeEntry : timeEntries) {
            timeEntriesResponse.add(TimeEntryMapper.timeEntryToTimeEntryResponseDTO(timeEntry));
        }
        return timeEntriesResponse;
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
            existingTimeEntry.setType(request.type());
            existingTimeEntry.setSubject(request.subject());
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

    public Page<TimeEntry> getByFilter(TaskType type, Long studentId, boolean expression, Pageable pageable) {
        return timeEntryRepository.findAll(
                TimeEntrySpecifications.filter(type, studentId, expression), pageable);
    }

    @Transactional
    @CacheEvict(value = "timeEntry", allEntries = true)
    public TimeEntryResponseDTO setTimeEnd(Long id) {
        TimeEntry timeEntry = timeEntryRepository.findById(id).map(existingTimeEntry -> {
            existingTimeEntry.setTimeEnd(LocalDateTime.now());
            LocalDateTime startTime = existingTimeEntry.getTimeStart();
            LocalDateTime endTime = existingTimeEntry.getTimeEnd();
            Duration duration = Duration.between(startTime, endTime);
            existingTimeEntry.setDuration((double) duration.toMinutes() / 60);
            return timeEntryRepository.save(existingTimeEntry);
        }).orElse(null);
        return TimeEntryMapper.timeEntryToTimeEntryResponseDTO(timeEntry);
    }
}
