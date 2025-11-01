package com.example.pos_backend.repository;

import com.example.pos_backend.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional; // Bu import'u ekleyin

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    // YENİ EKLENEN METOT: (DatabaseInitializerService'in ihtiyacı olan)
    Optional<Product> findByProductName(String productName);
}