package com.example.crossPlatform.model;

import java.time.LocalDateTime;

import com.example.crossPlatform.enums.TokenType;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private TokenType type;

    private String value;

    private LocalDateTime expiryDate;

    private boolean disabled;

    @ManyToOne
    private User user;

    public Token(TokenType type, String value, LocalDateTime expiryDate, boolean disabled, User user) {
        this.type = type;
        this.value = value;
        this.expiryDate = expiryDate;
        this.disabled = disabled;
        this.user = user;
    }
}
