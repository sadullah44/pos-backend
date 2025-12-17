package com.example.pos_backend.controller; // Paket adınızın bu olduğundan emin olun

import com.example.pos_backend.model.Category;
import com.example.pos_backend.service.CategoryService; // 'Kategori Beyni'ni import et
import org.springframework.web.bind.annotation.*;

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
    // EKLEME: POST http://localhost:8080/kategoriler
    @PostMapping
    public Category createCategory(@RequestBody Category category) {
        return categoryService.createCategory(category);
    }

    // GÜNCELLEME: PUT http://localhost:8080/kategoriler/{id}
    @PutMapping("/{id}")
    public Category updateCategory(@PathVariable Long id, @RequestBody Category category) {
        return categoryService.updateCategory(id, category);
    }

    // SİLME: DELETE http://localhost:8080/kategoriler/{id}
    @DeleteMapping("/{id}")
    public void deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
    }
}