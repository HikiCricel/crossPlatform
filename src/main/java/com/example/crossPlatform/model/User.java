package com.example.crossPlatform.model;

import java.util.Set;

public class User {
    private Long id;
    private String username;
    private String password;
    private boolean enabled;
    private Set<Role> roles;
}
