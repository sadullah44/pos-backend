// Paket adınız (örn: com.example.pos_backend.controller)
package com.example.pos_backend.controller;

import com.example.pos_backend.dto.AddProductRequest;
import com.example.pos_backend.model.Product;
import com.example.pos_backend.service.ProductService; // "Ürün Beyni"ni import et
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/urunler") // Ana adres (Android'in '@GET("urunler")' ile eşleşir)
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    /**
     * MEVCUT KAPI (Tüm Ürünleri Getirir)
     * Adres: GET /urunler
     * (Ana adres '@RequestMapping("/urunler")' olduğu için,
     * bu metot sadece '@GetMapping' kullanır)
     */
    @GetMapping
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }


    // --- YENİ EKLENEN KAPI (Adım 96 - Düzeltme) ---
    /**
     * "Mutfağa Ürün Ekleme" Akışı Adım 8:
     * Kategori ID'sine göre SADECE o kategorinin ürünlerini getirir.
     * Adres: GET /urunler/kategori/{id}
     *
     * Bu, Android'deki (Adım 94) '@GET("urunler/kategori/{kategoriId}")'
     * kapısının "Sunucu" tarafındaki karşılığıdır.
     */
    @GetMapping("/kategori/{kategoriId}") // '/urunler' (ana adres) + '/kategori/{id}'
    public List<Product> getProductsByCategoryId(@PathVariable Long kategoriId) {

        // KAPI (Controller), İŞİ BEYNE (Service) PASLAR
        // (Bu 'getProductsByCategoryId' metodu 'ProductService'de
        // 'kırmızı' görünecek, çünkü onu bir sonraki adımda (Bölüm 2)
        // ekleyeceğiz)
        return productService.getProductsByCategoryId(kategoriId);
    }
    // --- DÜZELTME BİTTİ ---
    @PostMapping("/ekle")
    public Product addProduct(@RequestBody AddProductRequest request) {
        return productService.addProduct(request);
    }


}