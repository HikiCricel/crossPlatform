package com.example.crossPlatform.specifications;

import java.time.LocalDateTime;

import org.springframework.data.jpa.domain.Specification;

import com.example.crossPlatform.enums.TaskType;
import com.example.crossPlatform.model.TimeEntry;

public class TimeEntrySpecifications {
    private static Specification<TimeEntry> studentIdLike(Long studentId) {
        return (root, query, criteriaBuilder) -> {
            if (studentId == null) {
                return null;
            }
            return criteriaBuilder.equal(root.get("studentId"), studentId);
        };
    }

    private static Specification<TimeEntry> typeLike(TaskType type) {
        return (root, query, criteriaBuilder) -> {
            if (type == null) {
                return null;
            }
            return criteriaBuilder.equal(root.get("type"), type);
        };
    }

    private static Specification<TimeEntry> timeDateStartLike(LocalDateTime timeStart) {
        return (root, query, criteriaBuilder) -> {
            if (timeStart == null) {
                return null;
            }
            return criteriaBuilder.equal(root.get("type"), timeStart);
        };
    }

    private static Specification<TimeEntry> timeDateEndLike(LocalDateTime timeEnd) {
        return (root, query, criteriaBuilder) -> {
            if (timeEnd == null) {
                return null;
            }
            return criteriaBuilder.equal(root.get("type"), timeEnd);
        };
    }

    private static Specification<TimeEntry> isBillableLike(boolean expr){
        return (root, query, criteriaBuilder) ->{
            return criteriaBuilder.equal(root.get("isBillable"), expr);
        };
    }

    public static Specification<TimeEntry> filter(TaskType type, Long studentId, boolean expression){
        return Specification.allOf(
            typeLike(type),
            studentIdLike(studentId),
            isBillableLike(expression)
        );
    }
}
