package com.example.crossPlatform.specifications;

import org.springframework.data.jpa.domain.Specification;

import com.example.crossPlatform.model.Student;

public class StudentSpecifications {
    private static Specification<Student> nameLike(String name) {
        return (root, query, criteriaBuilder) -> {
            if (name == null || name.trim().isEmpty()) {
                return null;
            }
            return criteriaBuilder.like(criteriaBuilder.lower(
                    root.get("name")), "%" + name.trim().toLowerCase() + "%");
        };
    }

    private static Specification<Student> groupLike(String group) {
        return (root, query, criteriaBuilder) -> {
            if (group == null || group.trim().isEmpty()) {
                return null;
            }
            return criteriaBuilder.like(criteriaBuilder.lower(
                    root.get("group")), "%" + group.trim().toLowerCase() + "%");
        };
    }

    public static Specification<Student> filter(String name, String group) {
        return Specification.allOf(nameLike(name), groupLike(group));
    }
}
