package com.example.pos_backend.service;

import com.example.pos_backend.dto.AddProductRequest;
import com.example.pos_backend.model.Category;
import com.example.pos_backend.model.Product;
import com.example.pos_backend.repository.CategoryRepository;
import com.example.pos_backend.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    // Fotoğrafların kaydedileceği klasör (Proje ana dizininde oluşur)
    private final Path fileStorageLocation = Paths.get("uploads");

    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;

        // Upload klasörü yoksa oluştur
        try {
            Files.createDirectories(fileStorageLocation);
        } catch (Exception ex) {
            throw new RuntimeException("Upload dizini oluşturulamadı!", ex);
        }
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public List<Product> getProductsByCategoryId(Long kategoriId) {
        Category category = categoryRepository.findById(kategoriId)
                .orElseThrow(() -> new RuntimeException("Hata: Kategori bulunamadı, ID: " + kategoriId));
        return productRepository.findByCategory(category);
    }

    // --- YENİ: FOTOĞRAFLI ÜRÜN EKLEME (Parametreler Genişletildi) ---
    @Transactional
    public Product addProductWithImage(String name, BigDecimal price, Integer stock, Long categoryId,
                                       String description, String cookingLevel, String spiceLevel, String saltLevel,
                                       MultipartFile imageFile) {
        // 1. Kategoriyi Bul
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Kategori bulunamadı!"));

        // 2. Ürünü Oluştur
        Product newProduct = new Product();
        newProduct.setProductName(name);
        newProduct.setBasePrice(price);
        newProduct.setCategory(category);
        newProduct.setStockQuantity(stock != null ? stock : 0);
        newProduct.setAvailable(true);
        newProduct.setKitchenItem(true);

        // --- YENİ ALANLARIN EKLENMESİ ---
        newProduct.setDescription(description);
        newProduct.setCookingLevel(cookingLevel);
        newProduct.setSpiceLevel(spiceLevel);
        newProduct.setSaltLevel(saltLevel);
        // --------------------------------

        // 3. Fotoğrafı Kaydet (Eğer varsa)
        if (imageFile != null && !imageFile.isEmpty()) {
            String fileName = saveFile(imageFile);
            newProduct.setImagePath(fileName);
        }

        return productRepository.save(newProduct);
    }

    // --- Dosya Kaydetme Yardımcı Metodu ---
    private String saveFile(MultipartFile file) {
        try {
            // Benzersiz bir dosya adı oluştur (çakışmayı önlemek için UUID)
            String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();

            // Hedef yol
            Path targetLocation = this.fileStorageLocation.resolve(fileName);

            // Dosyayı kopyala
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return fileName;
        } catch (IOException ex) {
            throw new RuntimeException("Dosya kaydedilemedi " + file.getOriginalFilename(), ex);
        }
    }

    // --- JSON ile Ekleme (DTO Kullanan Metot) ---
    @Transactional
    public Product addProduct(AddProductRequest request) {
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Kategori bulunamadı!"));

        Product newProduct = new Product();
        newProduct.setProductName(request.getProductName());
        newProduct.setBasePrice(request.getBasePrice());
        newProduct.setCategory(category);
        newProduct.setAvailable(true);
        newProduct.setKitchenItem(true);
        newProduct.setStockQuantity(request.getStock() != null ? request.getStock() : 0);

        // --- YENİ ALANLARIN EKLENMESİ ---
        newProduct.setDescription(request.getDescription());
        newProduct.setImagePath(request.getImagePath());
        newProduct.setCookingLevel(request.getCookingLevel());
        newProduct.setSpiceLevel(request.getSpiceLevel());
        newProduct.setSaltLevel(request.getSaltLevel());
        // --------------------------------

        return productRepository.save(newProduct);
    }

    // --- JSON ile Güncelleme (Android App Burayı Kullanıyor) ---
    @Transactional
    public Product updateProduct(Long productId, AddProductRequest request) {
        Product existingProduct = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Ürün bulunamadı: " + productId));

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new EntityNotFoundException("Kategori bulunamadı: " + request.getCategoryId()));

        // Temel Güncellemeler
        existingProduct.setProductName(request.getProductName());
        existingProduct.setBasePrice(request.getBasePrice());
        existingProduct.setCategory(category);

        if (request.getStock() != null) {
            existingProduct.setStockQuantity(request.getStock());
        }

        // --- YENİ ALANLARIN GÜNCELLENMESİ ---
        existingProduct.setDescription(request.getDescription());
        existingProduct.setCookingLevel(request.getCookingLevel());
        existingProduct.setSpiceLevel(request.getSpiceLevel());
        existingProduct.setSaltLevel(request.getSaltLevel());

        // Eğer request içinde yeni bir resim yolu string olarak gelirse güncelle
        if (request.getImagePath() != null) {
            existingProduct.setImagePath(request.getImagePath());
        }
        // ------------------------------------

        return productRepository.save(existingProduct);
    }

    // 2. YENİ METOT (MULTIPART / RESİMLİ GÜNCELLEME İÇİN - Parametreler Genişletildi)
    @Transactional
    public Product updateProductWithImage(Long productId, String name, BigDecimal price, Integer stock, Long categoryId,
                                          String description, String cookingLevel, String spiceLevel, String saltLevel,
                                          MultipartFile image) {
        // 1. Ürünü Bul
        Product existingProduct = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Ürün bulunamadı: " + productId));

        // 2. Kategoriyi Bul
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new EntityNotFoundException("Kategori bulunamadı: " + categoryId));

        // 3. Temel Bilgileri Güncelle
        existingProduct.setProductName(name);
        existingProduct.setBasePrice(price);
        existingProduct.setCategory(category);
        if (stock != null) {
            existingProduct.setStockQuantity(stock);
        }

        // --- YENİ ALANLARIN GÜNCELLENMESİ ---
        existingProduct.setDescription(description);
        existingProduct.setCookingLevel(cookingLevel);
        existingProduct.setSpiceLevel(spiceLevel);
        existingProduct.setSaltLevel(saltLevel);
        // --------------------------------

        // 4. Resim İşlemleri (Eğer yeni resim gönderildiyse)
        if (image != null && !image.isEmpty()) {
            try {
                // Kayıt klasörü
                String uploadDir = System.getProperty("user.dir") + File.separator + "uploads";
                Path uploadPath = Paths.get(uploadDir);
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                String originalFilename = image.getOriginalFilename();
                String extension = "";
                if (originalFilename != null && originalFilename.contains(".")) {
                    extension = originalFilename.substring(originalFilename.lastIndexOf("."));
                }
                String newFileName = UUID.randomUUID().toString() + extension;

                Path filePath = uploadPath.resolve(newFileName);
                Files.copy(image.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

                existingProduct.setImagePath(newFileName);

            } catch (IOException e) {
                throw new RuntimeException("Resim dosyası güncellenirken hata oluştu: " + e.getMessage());
            }
        }

        return productRepository.save(existingProduct);
    }

    @Transactional
    public void deleteProduct(Long productId) {
        if (!productRepository.existsById(productId)) {
            throw new EntityNotFoundException("Silinecek ürün bulunamadı: " + productId);
        }
        productRepository.deleteById(productId);
    }
}