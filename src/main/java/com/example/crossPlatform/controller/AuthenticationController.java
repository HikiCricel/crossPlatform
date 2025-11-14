package com.example.crossPlatform.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.crossPlatform.dto.ChangePasswordRequest;
import com.example.crossPlatform.dto.LoginRequest;
import com.example.crossPlatform.dto.LoginResponse;
import com.example.crossPlatform.dto.UserLogged;
import com.example.crossPlatform.service.AuthenticationService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;


@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {
private final AuthenticationService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request,
            @CookieValue(name = "access-token", required = false) String access,
            @CookieValue(name = "refresh-token", required = false) String refresh) {
        return authService.login(request, access, refresh);
    }

    @PostMapping("/refresh")
    public ResponseEntity<LoginResponse> refresh(@CookieValue(name = "refresh-token", required = true) String refresh) {
        return authService.refresh(refresh);
    }

    @PostMapping("/logout")
    public ResponseEntity<LoginResponse> logout(@CookieValue (name = "access-token", required = false) String access) {
        return authService.logout(access);
    }

    @GetMapping("/info")
    public ResponseEntity<UserLogged> info() {
        return ResponseEntity.ok(authService.info());
    }

    @PatchMapping("/changePassword")
    public ResponseEntity<LoginResponse> changePassword(ChangePasswordRequest request) {
        return authService.changePassword(request);
    }
}
