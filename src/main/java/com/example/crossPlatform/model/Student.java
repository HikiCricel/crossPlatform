package com.example.crossPlatform.model;

import java.util.List;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Size(min = 3, max = 50, message = "name")
    @Column(nullable = false, length = 100)
    private String name;
    @Size(min = 3, max = 50, message = "group")
    @Column(nullable = false, length = 100, name = "group_name")
    private String group;
    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TimeEntry> recentEntries;

    @ManyToMany
    @JoinTable(name = "student_deadlines", joinColumns = @JoinColumn(name = "student_id"), inverseJoinColumns = @JoinColumn(name = "deadline_id"))
    private Set<Deadline> deadlines;
}
