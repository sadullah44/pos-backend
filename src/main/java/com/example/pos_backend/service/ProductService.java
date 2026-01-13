// Paket adınız (örn: com.example.pos_backend.service)
package com.example.pos_backend.service;

import com.example.pos_backend.dto.AddProductRequest;
import com.example.pos_backend.model.Category;
import com.example.pos_backend.model.Product;
import com.example.pos_backend.repository.CategoryRepository;
import com.example.pos_backend.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    /**
     * Kurucu Metot (Constructor)
     */
    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    // --- Tüm Ürünleri Getir ---
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    // --- Kategoriye Göre Getir ---
    public List<Product> getProductsByCategoryId(Long kategoriId) {
        Category category = categoryRepository.findById(kategoriId)
                .orElseThrow(() -> new RuntimeException("Hata: Kategori bulunamadı, ID: " + kategoriId));
        return productRepository.findByCategory(category);
    }

    // --- YENİ ÜRÜN EKLEME ---
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

        // --- STOK KAYDI (YENİ EKLENDİ) ---
        // Eğer stok bilgisi girildiyse kaydet, girilmediyse 0 yap.
        if (request.getStock() != null) {
            newProduct.setStockQuantity(request.getStock());
        } else {
            newProduct.setStockQuantity(0);
        }
        // --------------------------------

        // 3. Kaydet ve Döndür
        return productRepository.save(newProduct);
    }

    // --- ÜRÜN GÜNCELLEME (Sorunun Çözüldüğü Yer) ---
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

        // --- EKSİK OLAN KISIM BURASIYDI ---
        // Android'den gelen stok bilgisini alıp veritabanına yazıyoruz.
        if (request.getStock() != null) {
            existingProduct.setStockQuantity(request.getStock());
        }
        // ----------------------------------

        // Mutfak ürünü mü değil mi güncellemek istersen:
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
        productRepository.deleteById(productId);
    }
}