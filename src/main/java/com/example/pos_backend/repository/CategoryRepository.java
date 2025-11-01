package com.example.pos_backend.repository;

import com.example.pos_backend.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional; // Bu import'u ekleyin

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    // YENİ EKLENEN METOT: (DatabaseInitializerService'in ihtiyacı olan)
    Optional<Category> findByCategoryName(String categoryName);
}