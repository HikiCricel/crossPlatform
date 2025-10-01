package com.example.crossPlatform.model;

import java.util.List;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    @Size(min = 3, max = 50, message = "name")
    @Column(nullable = false, unique = true, length = 100)
    private String name;
    @NotBlank
    @Size(min = 3, max = 50, message = "group")
    @Column(nullable = false, unique = true, length = 100, name = "group_name")
    private String group;
    @OneToMany()
    private List<TimeEntry> recentEntries;
}
