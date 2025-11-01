package com.example.pos_backend.controller; // Paket adınızın bu olduğundan emin olun

import com.example.pos_backend.model.Product;
import com.example.pos_backend.service.ProductService; // 'Ürün Beyni'ni import et
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin("*") // Android (veya Postman) tarafının erişebilmesi için
@RestController
@RequestMapping("/urunler") // Tüm ürün endpoint'lerinin başı '/api/urunler'
public class ProductController {

    // --- 'Beyin' katmanını (Service) enjekte etme ---

    private final ProductService productService;

    // Tek constructor, Spring otomatik olarak anlar
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // --- YENİ ENDPOINT (Arkadaşınızın İhtiyacı İçin) ---
    // Bu, "Tüm Ürünleri Getir" senaryosunu gerçekleştiren API kapısıdır.
    // URL: GET http://localhost:8080/api/urunler

    /**
     * Tüm ürünlerin listesini döndürür.
     * @return Veritabanındaki tüm 'Product' nesnelerinin bir listesi.
     */
    @GetMapping
    public List<Product> getAllProducts() {
        // KAPI (Controller), İŞİ BEYNE (Service) PASLAR
        return productService.getAllProducts();
    }
}