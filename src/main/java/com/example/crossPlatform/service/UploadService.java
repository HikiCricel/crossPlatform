package com.example.crossPlatform.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.crossPlatform.dto.DeadlineRequestDTO;
import com.example.crossPlatform.dto.StudentRequestDTO;
import com.example.crossPlatform.dto.UploadResponseDTO;
import com.example.crossPlatform.enums.TaskType;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UploadService {
    private static final Logger logger = LoggerFactory.getLogger(UploadService.class);

    private final StudentService studentService;
    private final DeadlineService deadlineService;

    @Value("${spring.servlet.multipart.loaction}")
    private String uploadLocation;

    private void validateFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }

        String filename = file.getOriginalFilename();
        if (filename == null || !filename.toLowerCase().endsWith(".csv")) {
            throw new IllegalArgumentException("The system supports only CSV files");
        }

        logger.info("File validation passed: {}", filename);
    }

    private Path saveFile(MultipartFile file) throws IOException {
        logger.debug("Attempting to save file: {}", file.getOriginalFilename());

        String timestamp = LocalDateTime.now().toString().replaceAll(":", "-");
        String filename = timestamp + "_" + file.getOriginalFilename();

        Path targetLocation = Paths.get(uploadLocation).toAbsolutePath().normalize().resolve(filename);
        logger.debug("File copying target location is setted up");

        Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
        logger.info("File saved to: {}", targetLocation);

        return targetLocation;
    }

    public UploadResponseDTO importStudents(MultipartFile file) {
        validateFile(file);
        int successCount = 0;
        int failureCount = 0;
        List<String> errorList = new ArrayList<>();

        try {
            try {
                validateFile(file);
            } catch (IllegalArgumentException e) {
                errorList.add(file.getOriginalFilename() + " : " + e.getMessage());
                throw new IllegalArgumentException("File validation failed");
            }

            Path savedFile = saveFile(file);

            CSVFormat format = CSVFormat.DEFAULT.builder().setHeader().setSkipHeaderRecord(true)
                    .setIgnoreHeaderCase(true).setTrim(true).get();

            try (BufferedReader reader = Files.newBufferedReader(savedFile, StandardCharsets.UTF_8);
                    CSVParser csvParser = format.parse(reader)) {
                for (CSVRecord record : csvParser) {
                    try {
                        StudentRequestDTO request = new StudentRequestDTO(record.get("name"), record.get("group"));

                        studentService.create(request);
                        successCount++;
                    } catch (Exception e) {
                        failureCount++;
                    }
                }
            }
            logger.info("Student import completed. Success: {}, Failures: {}", successCount, failureCount);
            return new UploadResponseDTO(successCount + failureCount, successCount, failureCount, errorList);
        } catch (IOException e) {
            throw new RuntimeException("Failed to process CSV file: " + e.getMessage());
        }
    }

    public UploadResponseDTO importDeadlines(MultipartFile file) {
        validateFile(file);
        int successCount = 0;
        int failureCount = 0;
        List<String> errorList = new ArrayList<>();

        try {
            try {
                validateFile(file);
            } catch (IllegalArgumentException e) {
                errorList.add(file.getOriginalFilename() + " : " + e.getMessage());
                throw new IllegalArgumentException("File validation failed");
            }

            Path savedFile = saveFile(file);

            CSVFormat format = CSVFormat.DEFAULT.builder().setHeader().setSkipHeaderRecord(true)
                    .setIgnoreHeaderCase(true).setTrim(true).get();

            try (BufferedReader reader = Files.newBufferedReader(savedFile, StandardCharsets.UTF_8);
                    CSVParser csvParser = format.parse(reader)) {
                for (CSVRecord record : csvParser) {
                    try {
                        DeadlineRequestDTO request = new DeadlineRequestDTO(record.get("subject"),
                                LocalDate.parse(record.get("deadline")),
                                TaskType.valueOf(record.get("type").toUpperCase()));

                        deadlineService.create(request);
                        successCount++;
                    } catch (Exception e) {
                        failureCount++;
                    }
                }
            }
            logger.info("Deadline import completed. Success: {}, Failures: {}", successCount, failureCount);
            return new UploadResponseDTO(successCount + failureCount, successCount, failureCount, errorList);
        } catch (IOException e) {
            throw new RuntimeException("Failed to process CSV file: " + e.getMessage());
        }
    }
}
