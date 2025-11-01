package com.example.pos_backend.dto; // 'dto' paketimiz

/**
 * Bu sınıf bir @Entity DEĞİLDİR.
 * Sadece 'POST /api/login' endpoint'ine gönderilen
 * JSON'u yakalamak için kullanılan bir Veri Taşıma Nesnesidir (DTO).
 */
public class LoginRequest {

    private String username;
    private String password;

    // JSON'u nesneye çevirmek (Deserialization) için
    // Spring'in (Jackson kütüphanesi) bu getter ve setter'lara ihtiyacı var.

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}