package com.example.crossPlatform.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.crossPlatform.model.TimeEntry;
import com.example.crossPlatform.repository.TimeEntryRepository;

@Service
public class TimeEntryService {
    private final TimeEntryRepository timeEntryRepository;
    
    public TimeEntryService(TimeEntryRepository timeEntryRepository) {
        this.timeEntryRepository = timeEntryRepository;
    }


    public List<TimeEntry> getAll() {
        return timeEntryRepository.findAll();
    }

    
    public List<TimeEntry> getAllByDescription(String description) {
        return timeEntryRepository.findAllByDescription(description);
    }

    public TimeEntry create(TimeEntry timeEntry) {
        return timeEntryRepository.save(timeEntry);
    }

    public TimeEntry getById(Long id) {
        // for (Student student : students) {
        //     if (student.getId().equals(id)) {
        //         return studentRepository.findById(id).orElse(null);
        //     }
        // }
        // return null;
        return timeEntryRepository.findById(id).orElse(null);
    }

    public TimeEntry update(Long id, TimeEntry timeEntry) {
        return timeEntryRepository.findById(id).map(existingTimeEntry -> {
            existingTimeEntry.setDescription(timeEntry.getDescription());
            existingTimeEntry.setStart(timeEntry.getStart());
            return timeEntryRepository.save(existingTimeEntry);
        }).orElse(null);
    }

    public boolean deleteById(Long id) {
        if (timeEntryRepository.existsById(id)) {
            timeEntryRepository.deleteById(id);
            return true;
        }
        return false;
    }  
}
