package com.example.pos_backend.model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.persistence.Table;

@Entity
@Table(name = "users") // Arkadaşınızın SQL'indeki 'users' tablo adı
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userID; // Arkadaşınızın SQL'indeki 'userID'

    @Column(length = 50, unique = true, nullable = false)
    private String username; // Arkadaşınızın SQL'indeki 'username'
    // Bu satır: "Android'den gelirken OKU, ama Android'e geri gönderirken GİZLE" demektir.
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name = "passwordHash", length = 255, nullable = false)
    private String password; // Arkadaşınızın SQL'indeki 'passwordHash' (Java'da 'password' diyelim)

    @Column(length = 100, nullable = false)
    private String fullName; // Arkadaşınızın SQL'inde eklediği YENİ ALAN

    @Column(nullable = false)
    @JsonProperty("isActive")
    private boolean isActive = true; // Arkadaşınızın SQL'indeki 'isActive' alanı

    // --- DEĞİŞİKLİK 1: 'String role' gitti, 'Role role' geldi ---
    // Birçok 'User' (Kullanıcı), bir 'Role' (Role) sahip olabilir.
    @ManyToOne
    @JoinColumn(name = "roleID", nullable = false) // Arkadaşınızın SQL'indeki 'roleID' Foreign Key'i
    private Role role;

    // --- DEĞİŞİKLİK 2: STATİK ROLLER SİLİNDİ ---
    // 'public static final String ROLE_CASHIER = "CASHIER";' gibi
    // eski sabit (static final) String'leri sildik.
    // Roller artık 'Roles' tablosundan gelecek.

    // --- Constructor (Yapıcı Metotlar) ---
    public User() {
    }

    // --- DEĞİŞİKLİK 3: Constructor güncellendi ---
    // 'DatabaseInitializerService'te kullanmak için
    // Artık 'String role' değil, 'Role role' nesnesi alıyor
    public User(String username, String password, String fullName, Role role, boolean isActive) {
        this.username = username;
        this.password = password; // (Bizim kodumuzda 'password' olarak kalsın)
        this.fullName = fullName;
        this.role = role;
        this.isActive = isActive;
    }


    // --- Getter ve Setter Metotları (Yeni alanlar için güncellendi) ---

    public Long getUserID() {
        return userID;
    }

    public void setUserID(Long userID) {
        this.userID = userID;
    }

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

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}