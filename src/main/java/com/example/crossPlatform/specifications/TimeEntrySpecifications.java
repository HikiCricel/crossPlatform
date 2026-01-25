package com.example.crossPlatform.specifications;

import org.springframework.data.jpa.domain.Specification;

import com.example.crossPlatform.enums.TaskType;
import com.example.crossPlatform.model.TimeEntry;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TimeEntrySpecifications {

    private static Specification<TimeEntry> typeLike(TaskType type) {
        return (root, query, criteriaBuilder) -> {
            if (type == null) {
                return null;
            }
            return criteriaBuilder.equal(root.get("type"), type);
        };
    }

    private static Specification<TimeEntry> isBillableLike(boolean isBillable) {
        return (root, query, criteriaBuilder) -> {
            return criteriaBuilder.equal(root.get("isBillable"), isBillable);
        };
    }

    public static Specification<TimeEntry> filter(TaskType type, boolean expression) {
        return Specification.allOf(
                typeLike(type),
                isBillableLike(expression));
    }
}
