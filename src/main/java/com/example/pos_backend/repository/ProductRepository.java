package com.example.pos_backend.repository;

import com.example.pos_backend.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    // İleride ürünleri kategoriye göre listelemek için
    // List<Product> findByCategory(Category category);
}