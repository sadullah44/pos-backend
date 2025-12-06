// Paket adınız (örn: com.example.pos_backend.service)
package com.example.pos_backend.service;

import com.example.pos_backend.dto.AddProductRequest;
import com.example.pos_backend.model.Category; // <-- 1. YENİ İMPORT
import com.example.pos_backend.model.Product;
import com.example.pos_backend.repository.CategoryRepository; // <-- 2. YENİ İMPORT
import com.example.pos_backend.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    // --- 3. YENİ DEĞİŞKEN (Kategori Müteahhiti) ---
    private final CategoryRepository categoryRepository;

    /**
     * 4. GÜNCELLENMİŞ Kurucu Metot (Constructor)
     * 'CategoryRepository'yi de 'inject' (dahil) et
     */
    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository; // <-- YENİ EKLENDİ
    }

    // --- (getAllProducts() metodu aynı kalır) ---
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    // --- 5. GÜNCELLENMİŞ METOT (getProductsByCategoryId) ---
    /**
     * Adım 97 (Düzeltme): "Sihirli" metot yerine "Akıllı" metot.
     * Bu metot, 'Controller'dan gelen 'kategoriId'yi (örn: 1L) alır.
     */
    public List<Product> getProductsByCategoryId(Long kategoriId) {

        // Adım 1: "Kategori Müteahhiti"ni kullanarak
        // 'kategoriId' (örn: 1L) ile 'Category' (Kategori) nesnesini
        // veritabanından bul. Bulamazsa, hata fırlat (güvenlik için).
        Category category = categoryRepository.findById(kategoriId)
                .orElseThrow(() -> new RuntimeException("Hata: Kategori bulunamadı, ID: " + kategoriId));

        // Adım 2: Artık elimizde 'Category' (Kategori) NESNESİ var.
        // Bu nesneyi "Ürün Müteahhiti"ne ver.
        // (Bu 'findByCategory' metodu 'ProductRepository'de
        // 'kırmızı' görünecek, çünkü onu bir sonraki adımda (Bölüm 2)
        // düzelteceğiz)
        return productRepository.findByCategory(category);
    }
    // YENİ METOT: Ürün Ekleme
    @Transactional
    public Product addProduct(AddProductRequest request) {
        // 1. Kategoriyi Bul
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Kategori bulunamadı!"));

        // 2. Yeni Ürünü Oluştur
        Product newProduct = new Product();
        newProduct.setProductName(request.getProductName());
        newProduct.setBasePrice(request.getBasePrice());
        newProduct.setCategory(category); // İlişkiyi kur
        newProduct.setAvailable(true);    // Varsayılan olarak satışta
        newProduct.setKitchenItem(true);  // Varsayılan olarak mutfak ürünü

        // 3. Kaydet ve Döndür
        return productRepository.save(newProduct);
    }
    // --- ÜRÜN GÜNCELLEME ---
    @Transactional
    public Product updateProduct(Long productId, AddProductRequest request) {
        // 1. Güncellenecek ürünü bul
        Product existingProduct = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Ürün bulunamadı: " + productId));

        // 2. Yeni kategoriyi bul (Eğer kategori değiştiyse)
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new EntityNotFoundException("Kategori bulunamadı: " + request.getCategoryId()));

        // 3. Verileri Güncelle
        existingProduct.setProductName(request.getProductName());
        existingProduct.setBasePrice(request.getBasePrice());
        existingProduct.setCategory(category);

        // Mutfak ürünü mü değil mi güncellemek istersen request'e o alanı da ekleyip buraya yazabilirsin.
        // existingProduct.setKitchenItem(request.isKitchenItem());

        // 4. Kaydet
        return productRepository.save(existingProduct);
    }

    // --- ÜRÜN SİLME ---
    @Transactional
    public void deleteProduct(Long productId) {
        // Ürün var mı kontrol et
        if (!productRepository.existsById(productId)) {
            throw new EntityNotFoundException("Silinecek ürün bulunamadı: " + productId);
        }

        // DİKKAT: Eğer bu ürün geçmiş siparişlerde kullanıldıysa,
        // veritabanı "Foreign Key Constraint" hatası verebilir.
        // Basit çözüm: Direkt silmektir. (Hata alırsan catch bloğu ekleriz)
        productRepository.deleteById(productId);
    }

}