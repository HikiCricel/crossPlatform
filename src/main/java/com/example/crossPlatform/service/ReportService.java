package com.example.crossPlatform.service;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jxls.transform.poi.JxlsPoiTemplateFillerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import com.example.crossPlatform.dto.DeadlineReportDTO;
import com.example.crossPlatform.dto.DeadlineResponseDTO;
import com.example.crossPlatform.dto.StudentResponseDTO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReportService {
    private final StudentService studentService;
    private final DeadlineService deadlineService;
    private final Logger logger = LoggerFactory.getLogger(ReportService.class);

    @Value("${report.template-location}")
    private Resource templateLocation;

    public ByteArrayResource getStudentDeadlineReport(Long studentId) {
        StudentResponseDTO student = studentService.getById(studentId);
        List<DeadlineResponseDTO> deadlines = deadlineService.getAllByStudentId(studentId);

        List<DeadlineReportDTO> deadlineReports = deadlines.stream()
                .map(d -> new DeadlineReportDTO(d.subject(), d.type().name(), d.deadline().toString())).toList();

        Map<String, Object> data = new HashMap<>();
        data.put("student", student);
        data.put("deadlines", deadlineReports);

        try (InputStream is = templateLocation.getInputStream()) {
            byte[] reportBytes = JxlsPoiTemplateFillerBuilder.newInstance().withTemplate(is).buildAndFill(data);

            logger.info("Successfully generate deadlines report for student ID: {}", studentId);
            return new ByteArrayResource(reportBytes);
        } catch (Exception e) {
            logger.error("Error genrating report for student ID: {}", studentId, e);
            throw new RuntimeException("Failed to generate student deadline report", e);
        }
    }
}
