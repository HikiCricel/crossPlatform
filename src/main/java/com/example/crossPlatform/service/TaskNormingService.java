package com.example.crossPlatform.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.example.crossPlatform.enums.TaskType;

@Component
public class TaskNormingService {
    private final Logger logger = LoggerFactory.getLogger(TaskNormingService.class);

    public double getRequiredHours(TaskType type) {
        logger.info("Successfully mapped type");
        return switch (type) {
            case EXAM_PREP -> 60.0;
            case PROJECT -> 40.0;
            default -> 10.0;
        };
    }
}
