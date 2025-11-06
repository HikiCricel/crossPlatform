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

import com.example.crossPlatform.enums.TaskType;
import com.example.crossPlatform.model.Student;
import com.example.crossPlatform.model.TimeEntry;
import com.example.crossPlatform.repository.TimeEntryRepository;
import com.example.crossPlatform.specifications.TimeEntrySpecifications;

@Service
@Transactional(readOnly = true)
public class TimeEntryService {
    private final TimeEntryRepository timeEntryRepository;

    public TimeEntryService(TimeEntryRepository timeEntryRepository) {
        this.timeEntryRepository = timeEntryRepository;
    }

    @Cacheable(value = "timeEntries", key = "#root.methodName")
    public List<TimeEntry> getAll() {
        return timeEntryRepository.findAll();
    }

    public List<TimeEntry> getAllByDescription(String description) {
        return timeEntryRepository.findAllByDescription(description);
    }

    @Transactional
    @CacheEvict(value = "timeEntry", allEntries = true)
    public TimeEntry create(TimeEntry timeEntry) {
        return timeEntryRepository.save(timeEntry);
    }

    @Cacheable(value = "timeEntry", key = "#id")
    public TimeEntry getById(Long id) {
        // for (Student student : students) {
        // if (student.getId().equals(id)) {
        // return studentRepository.findById(id).orElse(null);
        // }
        // }
        // return null;
        return timeEntryRepository.findById(id).orElse(null);
    }

    @Transactional
    @Caching(evict = { @CacheEvict(value = "timeEntries", allEntries = true),
            @CacheEvict(value = "timeEntry", key = "#id") })
    public TimeEntry update(Long id, TimeEntry timeEntry) {
        return timeEntryRepository.findById(id).map(existingTimeEntry -> {
            existingTimeEntry.setTimeStart(timeEntry.getTimeStart());
            existingTimeEntry.setTimeEnd(timeEntry.getTimeEnd());
            return timeEntryRepository.save(existingTimeEntry);
        }).orElse(null);
    }

    @Transactional
    @Caching(evict = {@CacheEvict(value = "timeEntries", allEntries = true), @CacheEvict(value = "timeEntry", key = "#id")})
    public boolean deleteById(Long id) {
        if (timeEntryRepository.existsById(id)) {
            timeEntryRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public Page<TimeEntry> getByFilter(Student student, TaskType type, LocalDateTime dateTimeStart, LocalDateTime dateTimeEnd, boolean expression, Pageable pageable){
        return timeEntryRepository.findAll(TimeEntrySpecifications.filter(type, student, dateTimeStart, dateTimeEnd, expression), pageable);
    }
}
