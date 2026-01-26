package com.example.pos_backend.dto;

import java.math.BigDecimal;

public class AddProductRequest {

    // Temel Bilgiler
    private String productName;
    private BigDecimal basePrice;
    private Integer stock;
    private Long categoryId;

    // --- YENİ EKLENEN ALANLAR (Android ile Eşleşmeli) ---
    private String description;
    private String imagePath;
    private String cookingLevel; // Az, Orta, Çok
    private String spiceLevel;   // Acı, Tatlı vs.
    private String saltLevel;    // Tuzlu, Tuzsuz vs.

    // --- GETTER VE SETTERLAR ---

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public BigDecimal getBasePrice() { return basePrice; }
    public void setBasePrice(BigDecimal basePrice) { this.basePrice = basePrice; }

    public Integer getStock() { return stock; }
    public void setStock(Integer stock) { this.stock = stock; }

    public Long getCategoryId() { return categoryId; }
    public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }

    // --- Yeni Getter/Setterlar ---

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getImagePath() { return imagePath; }
    public void setImagePath(String imagePath) { this.imagePath = imagePath; }

    public String getCookingLevel() { return cookingLevel; }
    public void setCookingLevel(String cookingLevel) { this.cookingLevel = cookingLevel; }

    public String getSpiceLevel() { return spiceLevel; }
    public void setSpiceLevel(String spiceLevel) { this.spiceLevel = spiceLevel; }

    public String getSaltLevel() { return saltLevel; }
    public void setSaltLevel(String saltLevel) { this.saltLevel = saltLevel; }
}