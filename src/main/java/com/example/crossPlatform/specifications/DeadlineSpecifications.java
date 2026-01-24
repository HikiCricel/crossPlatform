package com.example.crossPlatform.specifications;

import org.springframework.data.jpa.domain.Specification;

import com.example.crossPlatform.enums.TaskType;
import com.example.crossPlatform.model.Deadline;

public class DeadlineSpecifications {
    private static Specification<Deadline> typeLike(TaskType type) {
        return (root, query, criteriaBuilder) -> {
            if (type == null) {
                return null;
            }
            return criteriaBuilder.equal(root.get("type"), type);
        };
    }

    private static Specification<Deadline> studentIdLike(Long studentId) {
        return (root, query, criteriaBuilder) -> {
            if (studentId == null) {
                return null;
            }
            return criteriaBuilder.equal(root.get("studentId"), studentId);
        };
    }

    public static Specification<Deadline> filter(TaskType type, Long studentId) {
        return Specification.allOf(typeLike(type), studentIdLike(studentId));
    }
}
