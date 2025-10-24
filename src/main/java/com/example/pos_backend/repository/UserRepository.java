package com.example.pos_backend.repository; // Paket adınızın bu olduğundan emin olun

import com.example.pos_backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository // 1. Spring'e bunun bir Depo olduğunu belirtir
public interface UserRepository extends JpaRepository<User, Long> {
    // JpaRepository<Hangi Entity'yi yöneteceği, O Entity'nin ID'sinin Tipi>

    // 2. Spring Data JPA'nın sihirli metot oluşturma özelliği:
    // Sadece bu metodu tanımlayarak, Spring bizim için "username" (kullanıcı adı)
    // sütununa göre arama yapan SQL kodunu otomatik yazar.
    // Android'deki login (giriş) işlemi için bunu kullanacağız.
    Optional<User> findByUsername(String username);
}