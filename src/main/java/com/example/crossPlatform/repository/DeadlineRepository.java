package com.example.crossPlatform.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.crossPlatform.model.Deadline;

@Repository
public interface DeadlineRepository extends JpaRepository<Deadline, Long>, JpaSpecificationExecutor<Deadline> {
    List<Deadline> findAllByType(String type);

    @Query("SELECT d FROM Deadline d JOIN d.students s WHERE s.id = :studentId")
    List<Deadline> findAllByStudentId(Long id);
}
