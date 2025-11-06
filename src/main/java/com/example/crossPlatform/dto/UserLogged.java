package com.example.crossPlatform.dto;

import java.util.Set;

public record UserLogged(String username, String role, Set<String> permissions) {
    
}
