package com.example.pos_backend.dto;

import com.example.pos_backend.model.User;

public class LoginResponse {
    private String token;
    private User user;

    public LoginResponse(String token, User user) {
        this.token = token;
        this.user = user;
    }

    // Getter ve Setter'lar...
    public String getToken() { return token; }
    public User getUser() { return user; }
}