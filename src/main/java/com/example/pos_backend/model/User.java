package com.example.pos_backend.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userID;

    @Column(length = 50, unique = true, nullable = false)
    private String username;

    // Şifre sıfırlama ve iletişim için gerekli yeni alan
    @Column(length = 100, unique = true, nullable = false)
    private String email;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name = "passwordHash", length = 255, nullable = false)
    private String password;

    @Column(length = 100, nullable = false)
    private String fullName;

    @Column(nullable = false)
    @JsonProperty("isActive")
    private boolean isActive = true;

    // --- GÜVENLİK ALANLARI ---
    @Column(name = "failed_attempts", nullable = false, columnDefinition = "int default 0")
    private int failedAttempts = 0;

    @Column(name = "account_locked", nullable = false, columnDefinition = "boolean default false")
    private boolean accountLocked = false;

    // --- ŞİFRE SIFIRLAMA ALANLARI ---
    @Column(name = "reset_code", length = 6)
    private String resetCode;

    @Column(name = "reset_code_expires_at")
    private LocalDateTime resetCodeExpiresAt;

    @ManyToOne
    @JoinColumn(name = "roleID", nullable = false)
    private Role role;

    // --- Constructor (Yapıcı Metotlar) ---
    public User() {
    }

    // Constructor güncellendi: email eklendi
    public User(String username, String email, String password, String fullName, Role role, boolean isActive) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.fullName = fullName;
        this.role = role;
        this.isActive = isActive;
        this.failedAttempts = 0;
        this.accountLocked = false;
    }

    // --- Getter ve Setter Metotları ---

    public Long getUserID() { return userID; }
    public void setUserID(Long userID) { this.userID = userID; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    // Email Getter/Setter
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }

    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }

    public int getFailedAttempts() { return failedAttempts; }
    public void setFailedAttempts(int failedAttempts) { this.failedAttempts = failedAttempts; }

    public boolean isAccountLocked() { return accountLocked; }
    public void setAccountLocked(boolean accountLocked) { this.accountLocked = accountLocked; }

    public String getResetCode() { return resetCode; }
    public void setResetCode(String resetCode) { this.resetCode = resetCode; }

    public LocalDateTime getResetCodeExpiresAt() { return resetCodeExpiresAt; }
    public void setResetCodeExpiresAt(LocalDateTime resetCodeExpiresAt) { this.resetCodeExpiresAt = resetCodeExpiresAt; }
}