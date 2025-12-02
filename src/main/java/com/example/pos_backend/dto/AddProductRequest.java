package com.example.pos_backend.dto;

import java.math.BigDecimal;

public class AddProductRequest {
    private String productName;
    private BigDecimal basePrice;
    private Long categoryId; // Hangi kategoriye eklenecek?

    // Getter ve Setter'lar
    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public BigDecimal getBasePrice() { return basePrice; }
    public void setBasePrice(BigDecimal basePrice) { this.basePrice = basePrice; }

    public Long getCategoryId() { return categoryId; }
    public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }
}