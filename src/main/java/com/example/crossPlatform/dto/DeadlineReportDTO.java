package com.example.crossPlatform.dto;

public record DeadlineReportDTO(String subject, String type, String date) {
    public String getSubject() {
        return subject;
    }

    public String getType() {
        return type;
    }

    public String getDate() {
        return date;
    }
}
