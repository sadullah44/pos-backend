package com.example.pos_backend.model;

import java.math.BigDecimal;
import jakarta.persistence.*;
import jakarta.persistence.Table;

@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long productID;

    @Column(name = "product_name", nullable = false)
    private String productName;

    @Column(name = "base_price", precision = 10, scale = 2)
    private BigDecimal basePrice;

    @Column(name = "stock_quantity", nullable = false, columnDefinition = "integer default 0")
    private Integer stockQuantity = 0;

    // --- YENİ EKLENEN ALAN: FOTOĞRAF YOLU ---
    @Column(name = "image_path")
    private String imagePath;
    // ----------------------------------------

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = true)
    private Category category;

    @Column(name = "is_kitchen_item")
    private boolean isKitchenItem;

    @Column(name = "is_available")
    private boolean isAvailable;

    public Product() {
    }

    // --- Getter ve Setter Metotları ---
    public Long getProductID() { return productID; }
    public void setProductID(Long productID) { this.productID = productID; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public BigDecimal getBasePrice() { return basePrice; }
    public void setBasePrice(BigDecimal basePrice) { this.basePrice = basePrice; }

    public Integer getStockQuantity() { return stockQuantity; }
    public void setStockQuantity(Integer stockQuantity) { this.stockQuantity = stockQuantity; }

    public Category getCategory() { return category; }
    public void setCategory(Category category) { this.category = category; }

    public boolean isKitchenItem() { return isKitchenItem; }
    public void setKitchenItem(boolean kitchenItem) { this.isKitchenItem = kitchenItem; }

    public boolean isAvailable() { return isAvailable; }
    public void setAvailable(boolean available) { this.isAvailable = available; }

    // --- YENİ GETTER/SETTER ---
    public String getImagePath() { return imagePath; }
    public void setImagePath(String imagePath) { this.imagePath = imagePath; }
}