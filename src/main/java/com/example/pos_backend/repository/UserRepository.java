package com.example.pos_backend.repository;

import com.example.pos_backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional; // Bu import'u ekleyin

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // YENİ EKLENEN METOT: (DatabaseInitializerService'in ihtiyacı olan)
    Optional<User> findByUsername(String username);
}