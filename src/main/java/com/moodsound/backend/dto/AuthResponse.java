package com.moodsound.backend.dto;

import java.time.LocalDateTime;

public class AuthResponse {
    private Integer userId;
    private String username;
    private String email;
    private String fullName;
    private String token;
    private String message;
    private LocalDateTime createdAt; // ⬅️ AÑADIDO

    public AuthResponse() {
    }

    // Constructor con createdAt
    public AuthResponse(Integer userId, String username, String email, String fullName, String token, LocalDateTime createdAt) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.fullName = fullName;
        this.token = token;
        this.createdAt = createdAt;
    }

    // Constructor sin createdAt (para compatibilidad)
    public AuthResponse(Integer userId, String username, String email, String fullName, String token) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.fullName = fullName;
        this.token = token;
    }

    // Getters y Setters
    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}