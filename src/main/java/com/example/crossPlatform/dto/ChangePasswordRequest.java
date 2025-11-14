package com.example.crossPlatform.dto;

public record ChangePasswordRequest(String oldPassword,
        String newPassword,
        String newAgain) {

}
