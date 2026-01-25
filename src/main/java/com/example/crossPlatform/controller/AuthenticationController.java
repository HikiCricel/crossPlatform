package com.example.crossPlatform.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.crossPlatform.dto.ChangePasswordRequest;
import com.example.crossPlatform.dto.LoginRequest;
import com.example.crossPlatform.dto.LoginResponse;
import com.example.crossPlatform.dto.UserLogged;
import com.example.crossPlatform.service.AuthenticationService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Methods for user authentication and authorization")
public class AuthenticationController {
    private final AuthenticationService authService;
    private static final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

    @Operation(summary = "Login User", description = "Authenticates user credentials and returns JWT token")
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request,
            @CookieValue(name = "access-token", required = false) String access,
            @CookieValue(name = "refresh-token", required = false) String refresh) {
        logger.info("Received request to login user {}", request.username());

        try {
            return authService.login(request, access, refresh);
        } catch (Exception e) {
            logger.error("Error while logging in user {}. Error: {}", request.username(), e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @Operation(summary = "Refresh User tokens", description = "Checks User tokens and refreshes the if needed")
    @PostMapping("/refresh")
    public ResponseEntity<LoginResponse> refresh(
            @CookieValue(name = "refresh-token", required = false) String refresh) {
        logger.info("Received request to refresh tokens");

        try {
            return authService.refresh(refresh);
        } catch (Exception e) {
            logger.error("Error while refreshing tokens: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @Operation(summary = "Logout User", description = "Revokes User tokens and logs him out")
    @PostMapping("/logout")
    public ResponseEntity<LoginResponse> logout(@CookieValue(name = "access-token", required = false) String access) {
        logger.info("Received request to logout user");

        try {
            return authService.logout(access);
        } catch (Exception e) {
            logger.error("Error while logging out user: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @Operation(summary = "Get User info", description = "Shows info about authorized User")
    @GetMapping("/info")
    public ResponseEntity<UserLogged> info() {
        logger.info("Received request to show info about user");

        try {
            return ResponseEntity.ok(authService.info());
        } catch (Exception e) {
            logger.error("Error while showing info about user: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @Operation(summary = "Change User password", description = "Changes User password to new one and logs him out after it")
    @PatchMapping("/changePassword")
    public ResponseEntity<LoginResponse> changePassword(ChangePasswordRequest request) {
        logger.info("Received request to change password");

        try {
            return authService.changePassword(request);
        } catch (Exception e) {
            logger.error("Error while changing user's password: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
