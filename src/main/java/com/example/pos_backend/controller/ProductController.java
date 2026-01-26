package com.example.pos_backend.controller;

import com.example.pos_backend.dto.AddProductRequest;
import com.example.pos_backend.model.Product;
import com.example.pos_backend.service.ProductService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/urunler")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // --- LİSTELEME İŞLEMLERİ (Aynı kaldı) ---
    @GetMapping
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/kategori/{kategoriId}")
    public List<Product> getProductsByCategoryId(@PathVariable Long kategoriId) {
        return productService.getProductsByCategoryId(kategoriId);
    }

    // --- EKLEME İŞLEMLERİ (GÜNCELLENDİ) ---

    // 1. JSON ile Ekleme (DTO güncellendiği için burası otomatik olarak yeni alanları kapsar)
    @PostMapping(value = "/ekle", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Product addProductJson(@RequestBody AddProductRequest request) {
        return productService.addProduct(request);
    }

    // 2. Multipart/Form-Data ile Ekleme (Resim + Veri) -> YENİ PARAMETRELER EKLENDİ
    @PostMapping(value = "/ekle", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Product addProductWithImage(
            @RequestParam("name") String name,
            @RequestParam("price") BigDecimal price,
            @RequestParam("stock") Integer stock,
            @RequestParam("categoryId") Long categoryId,
            // Yeni Eklenen Alanlar (Zorunlu değil olarak işaretledim, boş gelebilir)
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "cookingLevel", required = false) String cookingLevel,
            @RequestParam(value = "spiceLevel", required = false) String spiceLevel,
            @RequestParam(value = "saltLevel", required = false) String saltLevel,
            @RequestParam(value = "image", required = false) MultipartFile image
    ) {
        // Servisteki güncellenmiş metoda yönlendiriyoruz
        return productService.addProductWithImage(name, price, stock, categoryId, description, cookingLevel, spiceLevel, saltLevel, image);
    }

    // --- GÜNCELLEME İŞLEMLERİ (GÜNCELLENDİ) ---

    // SENARYO 1: Sadece metin güncelleme (JSON ile)
    // AddProductRequest güncellendiği için burası da artık yeni verileri kabul eder.
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Product updateProductJson(@PathVariable Long id, @RequestBody AddProductRequest request) {
        return productService.updateProduct(id, request);
    }

    // SENARYO 2: Resimli veya Resimsiz Multipart güncelleme - YENİ YÖNTEM
    // Android tarafında resim değiştirmek istersen veya Multipart gönderirsen burası çalışır.
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Product updateProductMultipart(
            @PathVariable Long id,
            @RequestParam("name") String name,
            @RequestParam("price") BigDecimal price,
            @RequestParam("stock") Integer stock,
            @RequestParam("categoryId") Long categoryId,
            // Yeni Eklenen Alanlar
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "cookingLevel", required = false) String cookingLevel,
            @RequestParam(value = "spiceLevel", required = false) String spiceLevel,
            @RequestParam(value = "saltLevel", required = false) String saltLevel,
            @RequestParam(value = "image", required = false) MultipartFile image
    ) {
        // Parametreleri Service'deki güncellenmiş metoda yönlendiriyoruz
        return productService.updateProductWithImage(id, name, price, stock, categoryId, description, cookingLevel, spiceLevel, saltLevel, image);
    }

    // --- SİLME ---
    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
    }
}