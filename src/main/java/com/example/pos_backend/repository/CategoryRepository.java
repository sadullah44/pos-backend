package com.example.pos_backend.repository;

import com.example.pos_backend.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    // İleride "Çorbalar" kategorisini adıyla bulmak için
    // Category findByCategoryName(String categoryName);
}