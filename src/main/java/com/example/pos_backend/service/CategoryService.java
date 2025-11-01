package com.example.pos_backend.service; // Paket adınızın bu olduğundan emin olun

import com.example.pos_backend.model.Category;
import com.example.pos_backend.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service // Bu sınıfın bir "Servis" (Beyin) katmanı olduğunu Spring'e bildirir.
public class CategoryService {

    // --- 'Müteahhit' katmanını (Repository) enjekte etme ---

    private final CategoryRepository categoryRepository;

    // Tek constructor (Yapıcı Metot), Spring @Autowired'e gerek duymadan anlar
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    /**
     * YENİ METOT (Arkadaşınızın İhtiyacı İçin)
     * Veritabanındaki tüm kategorileri getirir.
     */
    public List<Category> getAllCategories() {
        // Doğrudan 'Müteahhit'e (Repository) git ve hepsini bul
        return categoryRepository.findAll();
    }

    // TODO (İleride Gerekirse):
    // public Category getCategoryById(Long id) { ... }
}