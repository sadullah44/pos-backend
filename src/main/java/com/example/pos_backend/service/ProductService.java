package com.example.pos_backend.service; // Paket adınızın bu olduğundan emin olun

import com.example.pos_backend.model.Product;
import com.example.pos_backend.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service // Bu sınıfın bir "Servis" (Beyin) katmanı olduğunu Spring'e bildirir.
public class ProductService {

    // --- 'Müteahhit' katmanını (Repository) enjekte etme ---

    private final ProductRepository productRepository;

    // Tek constructor (Yapıcı Metot)
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    /**
     * YENİ METOT (Arkadaşınızın İhtiyacı İçin)
     * Veritabanındaki tüm ürünleri getirir.
     */
    public List<Product> getAllProducts() {
        // Doğrudan 'Müteahhit'e (Repository) git ve hepsini bul
        return productRepository.findAll();
    }

    // TODO (İleride Gerekirse):
    // Arkadaşınızın "Kategoriye göre filtreleme" yapması gerekirse,
    // buraya 'public List<Product> getProductsByCategoryId(Long categoryId)'
    // gibi bir metot eklememiz gerekecek.
}