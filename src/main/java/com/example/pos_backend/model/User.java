package com.example.pos_backend.model; // Paket adınızın bu olduğundan emin olun

// JPA (veritabanı) için gerekli importlar
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity // 1. Bu sınıfın bir veritabanı tablosu olduğunu belirtir
@Table(name = "users") // 2. Veritabanındaki tablonun adını "users" yapar
public class User {

    @Id // 3. Bu alanın "Primary Key" (Birincil Anahtar) olduğunu belirtir
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 4. ID'lerin otomatik artan sayı olmasını sağlar (1, 2, 3...)
    private Long id;

    private String username;
    private String password;
    private String role; // Rol: "KASA", "GARSON", "MUTFAK"

    // 5. ROL SABİTLERİ (Android projenizdeki gibi)
    public static final String ROLE_CASHIER = "KASA";
    public static final String ROLE_WAITER = "GARSON";
    public static final String ROLE_KITCHEN = "MUTFAK";

    // 6. JPA'nın bu sınıfı kullanabilmesi için BOŞ CONSTRUCTOR şarttır
    public User() {
    }

    // Orijinal constructor'ınız (veri eklemek için kullanışlı)
    public User(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    // 7. JPA'nın alanlara erişebilmesi için GETTER ve SETTER metotları şarttır
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}