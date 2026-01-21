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

    // --- EKLEME İŞLEMLERİ (Aynı kaldı) ---
    @PostMapping(value = "/ekle", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Product addProductJson(@RequestBody AddProductRequest request) {
        return productService.addProduct(request);
    }

    @PostMapping(value = "/ekle", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Product addProductWithImage(
            @RequestParam("name") String name,
            @RequestParam("price") BigDecimal price,
            @RequestParam("stock") Integer stock,
            @RequestParam("categoryId") Long categoryId,
            @RequestParam(value = "image", required = false) MultipartFile image
    ) {
        return productService.addProductWithImage(name, price, stock, categoryId, image);
    }

    // --- GÜNCELLEME İŞLEMLERİ (GÜNCELLENDİ) ---

    // SENARYO 1: Sadece metin güncelleme (JSON ile) - ESKİ YÖNTEM
    // Content-Type: application/json
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Product updateProductJson(@PathVariable Long id, @RequestBody AddProductRequest request) {
        return productService.updateProduct(id, request);
    }

    // SENARYO 2: Resimli veya Resimsiz Multipart güncelleme - YENİ YÖNTEM (Android İçin)
    // Content-Type: multipart/form-data
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Product updateProductMultipart(
            @PathVariable Long id,
            @RequestParam("name") String name,
            @RequestParam("price") BigDecimal price,
            @RequestParam("stock") Integer stock,
            @RequestParam("categoryId") Long categoryId,
            @RequestParam(value = "image", required = false) MultipartFile image
    ) {
        // Parametreleri Service'deki yeni metoda yönlendiriyoruz
        return productService.updateProductWithImage(id, name, price, stock, categoryId, image);
    }

    // --- SİLME ---
    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
    }
}