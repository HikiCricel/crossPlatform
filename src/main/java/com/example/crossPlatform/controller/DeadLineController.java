package com.example.crossPlatform.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.crossPlatform.dto.DeadlineRequestDTO;
import com.example.crossPlatform.dto.DeadlineResponseDTO;
import com.example.crossPlatform.service.DeadlineService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class DeadlineController {
    private final DeadlineService deadlineService;
    public DeadlineController(DeadlineService deadlineService){
        this.deadlineService = deadlineService;
    }

    @PostMapping("/deadlines")
    public ResponseEntity<DeadlineResponseDTO> addDeadline(@RequestBody @Valid DeadlineRequestDTO deadline) {
        return ResponseEntity.status(HttpStatus.CREATED).body(deadlineService.create(deadline));
    }

    @GetMapping("/deadLines")
    public List<DeadlineResponseDTO> getDeadlines(@RequestParam(required = false) String type) {
        if(type == null) 
            return deadlineService.getAll();
        else 
            return deadlineService.getAllByType(type);
    }

    @GetMapping("/deadLines/{id}")
    public ResponseEntity<DeadlineResponseDTO> getDeadline(@PathVariable Long id) {
        return ResponseEntity.ok().body(deadlineService.getById(id));
    }

    @PatchMapping("/deadlines/{id}")
    public ResponseEntity<Object> editStudent(@PathVariable Long id, @RequestBody DeadlineRequestDTO deadline) {
        DeadlineResponseDTO updated = deadlineService.update(id, deadline);
        if (updated != null) {
            return ResponseEntity.ok(updated);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/deadlines/{id}")
    public ResponseEntity<Void> deleteDeadline(@PathVariable Long id){
        if(deadlineService.deleteById(id)){
            ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok().build();
    }
}
