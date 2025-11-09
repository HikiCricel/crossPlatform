package com.example.crossPlatform.service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.crossPlatform.dto.LoginRequest;
import com.example.crossPlatform.dto.LoginResponse;
import com.example.crossPlatform.dto.UserLogged;
import com.example.crossPlatform.jwt.JwtTokenProvider;
import com.example.crossPlatform.mapper.UserMapper;
import com.example.crossPlatform.model.Token;
import com.example.crossPlatform.model.User;
import com.example.crossPlatform.repository.TokenRepository;
import com.example.crossPlatform.repository.UserRepository;
import com.example.crossPlatform.utils.CookieUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    public final UserRepository userRepository;
    public final TokenRepository tokenRepository;
    public final JwtTokenProvider jwtTokenProvider;
    public final CookieUtil cookieUtil;
    public final AuthenticationManager authenticationManager;
    public final UserService userService;

    @Value("${jwt.access.duration.minutes}")
    private long accessDurationMinute;
    @Value("${jwt.access.duration.seconds}")
    private long accessDurationSecond;
    @Value("${jwt.refresh.duration.days}")
    private long refreshDurationDay;
    @Value("${jwt.refresh.duration.seconds}")
    private long refreshDurationSecond;

    private void addAccessTokenCookie(HttpHeaders headers, Token token) {

        headers.add(HttpHeaders.SET_COOKIE,
                cookieUtil.createAccessCookie(token.getValue(), accessDurationSecond).toString());
    }

    private void addRefreshTokenCookie(HttpHeaders headers, Token token) {
        headers.add(HttpHeaders.SET_COOKIE,
                cookieUtil.createRefreshCookie(token.getValue(), refreshDurationSecond).toString());
    }

    private void revokeAllTokens(User user) {
        Set<Token> tokens = user.getTokens();
        tokens.forEach(token -> {
            if (token.getExpiryDate().isBefore(LocalDateTime.now())) {
                tokenRepository.delete(token);
            } else if (!token.isDisabled()) {
                token.setDisabled(true);
                tokenRepository.save(token);
            }
        });
    }

    public ResponseEntity<LoginResponse> login(LoginRequest request, String access, String refresh) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(request.username(), request.password()));
        User user = userService.getUser(request.username());
        boolean accessValid = jwtTokenProvider.isValid(access);
        boolean refreshValid = jwtTokenProvider.isValid(refresh);
        HttpHeaders headers = new HttpHeaders();
        revokeAllTokens(user);

        if (!accessValid) {
            Token newAccess = jwtTokenProvider.generateAccessToken(Map.of("role", user.getRole().getAuthority()),
                    accessDurationMinute, ChronoUnit.MINUTES, user);
            newAccess.setUser(user);
            addAccessTokenCookie(headers, newAccess);
            tokenRepository.save(newAccess);
        }

        if (!refreshValid) {
            Token newRefresh = jwtTokenProvider.generateRefreshToken(refreshDurationDay, ChronoUnit.DAYS, user);
            newRefresh.setUser(user);
            addRefreshTokenCookie(headers, newRefresh);
            tokenRepository.save(newRefresh);
        }
        SecurityContextHolder.getContext().setAuthentication(authentication);
        LoginResponse loginResponse = new LoginResponse(true, user.getRole().getName());
        return ResponseEntity.ok().headers(headers).body(loginResponse);
    }

    public ResponseEntity<LoginResponse> refresh(String refreshToken) {
        if (!jwtTokenProvider.isValid(refreshToken)) {
            throw new RuntimeException("token is invalid");
        }
        User user = userService.getUser(jwtTokenProvider.getUsername(refreshToken));
        HttpHeaders headers = new HttpHeaders();
        Token newAccess = jwtTokenProvider.generateAccessToken(Map.of("role", user.getRole().getAuthority()),
                accessDurationMinute, ChronoUnit.MINUTES, user);
        newAccess.setUser(user);
        tokenRepository.save(newAccess);
        addAccessTokenCookie(headers, newAccess);
        LoginResponse loginResponse = new LoginResponse(true, user.getRole().getName());
        return ResponseEntity.ok().headers(headers).body(loginResponse);
    }

    public ResponseEntity<LoginResponse> logout(String access, String refresh) {
        SecurityContextHolder.clearContext();
        User user = userService.getUser(jwtTokenProvider.getUsername(access));
        revokeAllTokens(user);
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.SET_COOKIE, cookieUtil.deleteAccessCookie().toString());
        headers.add(HttpHeaders.SET_COOKIE, cookieUtil.deleteRefreshCookie().toString());
        return ResponseEntity.ok().headers(headers).body(new LoginResponse(false, null));
    }

    public UserLogged info() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth instanceof AnonymousAuthenticationToken) {
            throw new RuntimeException("No user");
        }
        User user = userService.getUser(auth.getName());
        return UserMapper.userToUserLoggedDTO(user);
    }
}
