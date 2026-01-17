package com.example.crossPlatform.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.crossPlatform.enums.TaskType;
import com.example.crossPlatform.model.TimeEntry;

@Repository
public interface TimeEntryRepository extends JpaRepository<TimeEntry, Long>, JpaSpecificationExecutor<TimeEntry> {
    List<TimeEntry> findByDescriptionStartingWithIgnoreCase(String description);

    List<TimeEntry> findAllByDescription(String description);

    List<TimeEntry> findAllByType(TaskType type);

    List<TimeEntry> findAllByStudentId(Long studentId);

    @Query("""
            SELECT COALESCE(SUM(te.duration), 0.0)
            FROM TimeEntry te
            WHERE te.student.id = :studentId
              AND te.subject = :subject
              AND te.type = :type
            """)
    Double sumDurationByStudentAndSubjectAndType(
            @Param("studentId") Long studentId,
            @Param("subject") String subject,
            @Param("type") TaskType type);
}
