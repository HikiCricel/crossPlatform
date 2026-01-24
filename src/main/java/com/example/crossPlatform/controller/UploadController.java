package com.example.crossPlatform.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.crossPlatform.dto.UploadResponseDTO;
import com.example.crossPlatform.service.UploadService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/upload")
@RequiredArgsConstructor
public class UploadController {
    private static final Logger logger = LoggerFactory.getLogger(UploadController.class);
    private final UploadService uploadService;

    @PostMapping(value = "/students", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UploadResponseDTO> uploadStudents(@RequestParam MultipartFile file) {
        logger.info("Received request to upload file with Students: {}", file.getOriginalFilename());

        try {
            UploadResponseDTO response = uploadService.importStudents(file);
            if (response.failureCount() > 0) {
                return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT).body(response);
            }
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            logger.warn("Invalid file upload request: {}", e.getMessage());

            return ResponseEntity.badRequest().body(new UploadResponseDTO(0, 0, 0, null));
        } catch (Exception e) {
            logger.error("Error while uploading file: {}", e.getMessage(), e);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new UploadResponseDTO(
                    0, 0, 0, null));
        }
    }

    @PostMapping(value = "/deadlines", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UploadResponseDTO> uploadDeadlines(@RequestParam MultipartFile file) {
        logger.info("Received request to upload file with Deadlines: {}", file.getOriginalFilename());

        try {
            UploadResponseDTO response = uploadService.importDeadlines(file);
            if (response.failureCount() > 0) {
                return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT).body(response);
            }
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            logger.warn("Invalid file upload request: {}", e.getMessage());

            return ResponseEntity.badRequest().body(new UploadResponseDTO(0, 0, 0, null));
        } catch (Exception e) {
            logger.error("Error while uploading file: {}", e.getMessage(), e);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new UploadResponseDTO(
                    0, 0, 0, null));
        }
    }
}
