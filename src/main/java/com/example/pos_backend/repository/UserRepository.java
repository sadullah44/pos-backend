package com.example.pos_backend.repository;

import com.example.pos_backend.model.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Kullanıcı adına göre kullanıcı bul
     */
    Optional<User> findByUsername(String username);

    /**
     * Başarısız giriş denemesi sayısını artır
     */
    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.failedAttempts = u.failedAttempts + 1 WHERE u.userID = :userId")
    void incrementFailedAttempts(@Param("userId") Long userId);

    /**
     * Hesabı kilitle
     */
    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.accountLocked = true WHERE u.userID = :userId")
    void lockAccount(@Param("userId") Long userId);

    /**
     * Başarısız giriş denemelerini sıfırla
     */
    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.failedAttempts = 0 WHERE u.userID = :userId")
    void resetFailedAttempts(@Param("userId") Long userId);

    /**
     * Hesap kilidini aç
     */
    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.accountLocked = false WHERE u.userID = :userId")
    void unlockAccount(@Param("userId") Long userId);
}