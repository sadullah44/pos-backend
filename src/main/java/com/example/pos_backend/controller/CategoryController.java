package com.example.pos_backend.controller; // Paket adınızın bu olduğundan emin olun

import com.example.pos_backend.model.Category;
import com.example.pos_backend.service.CategoryService; // 'Kategori Beyni'ni import et
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin("*") // Android (veya Postman) tarafının erişebilmesi için
@RestController
@RequestMapping("/kategoriler") // Tüm kategori endpoint'lerinin başı '/api/kategoriler'
public class CategoryController {

    // --- 'Beyin' katmanını (Service) enjekte etme ---

    private final CategoryService categoryService;

    // Tek constructor, Spring otomatik olarak anlar
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    // --- YENİ ENDPOINT (Arkadaşınızın İhtiyacı İçin) ---
    // Bu, "Tüm Kategorileri Getir" senaryosunu gerçekleştiren API kapısıdır.
    // URL: GET http://localhost:8080/api/kategoriler

    /**
     * Tüm kategorilerin listesini döndürür.
     * @return Veritabanındaki tüm 'Category' nesnelerinin bir listesi.
     */
    @GetMapping
    public List<Category> getAllCategories() {
        // KAPI (Controller), İŞİ BEYNE (Service) PASLAR
        return categoryService.getAllCategories();
    }
}