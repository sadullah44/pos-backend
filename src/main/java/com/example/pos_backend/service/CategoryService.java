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

    // Kategori Ekleme
    public Category createCategory(Category category) {
        return categoryRepository.save(category);
    }

    // Kategori Güncelleme
    public Category updateCategory(Long id, Category categoryDetails) {
        return categoryRepository.findById(id).map(category -> {
            category.setCategoryName(categoryDetails.getCategoryName());
            // İsterseniz sortOrder da güncellenebilir
            // category.setSortOrder(categoryDetails.getSortOrder());
            return categoryRepository.save(category);
        }).orElse(null);
    }

    // Kategori Silme
    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }
}