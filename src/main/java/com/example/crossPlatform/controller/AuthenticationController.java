package com.example.crossPlatform.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.crossPlatform.dto.LoginRequest;
import com.example.crossPlatform.dto.LoginResponse;
import com.example.crossPlatform.service.AuthenticationService;

import lombok.RequiredArgsConstructor;

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
}
