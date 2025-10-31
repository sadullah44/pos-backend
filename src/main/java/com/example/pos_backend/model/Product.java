package com.example.pos_backend.model; // Paket adınızın bu olduğundan emin olun
import java.math.BigDecimal; // Bu import'u ekleyin
import jakarta.persistence.*;
import jakarta.persistence.Table;

@Entity
@Table(name = "products") // Arkadaşınızın 'Products' tablosu
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id") // Arkadaşınızın alanı: productID
    private Long productID;

    @Column(name = "product_name", nullable = false) // Arkadaşınızın alanı: productName
    private String productName;

    @Column(name = "base_price",precision = 10, scale = 2) // Arkadaşınızın alanı: basePrice
    private BigDecimal basePrice;

    // Arkadaşınızın alanı: categoryID (Hangi kategoriye ait)
    // --- GERÇEK İLİŞKİ ---
    // Az önce (Adım 17'de) oluşturduğumuz 'Category' yer tutucusuna bağlanıyoruz.
    @ManyToOne
    @JoinColumn(name = "category_id", nullable = true) // Kategorisiz ürün olabilir (?) diye 'nullable = true'
    private Category category;

    @Column(name = "is_kitchen_item") // Arkadaşınızın alanı: isKitchenItem
    private boolean isKitchenItem;

    @Column(name = "is_available") // Arkadaşınızın alanı: isAvailable
    private boolean isAvailable;

    // JPA için boş constructor
    public Product() {
    }

    // --- Getter ve Setter Metotları ---

    public Long getProductID() {
        return productID;
    }

    public void setProductID(Long productID) {
        this.productID = productID;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public BigDecimal getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(BigDecimal basePrice) {
        this.basePrice = basePrice;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public boolean isKitchenItem() {
        return isKitchenItem;
    }

    public void setKitchenItem(boolean kitchenItem) {
        this.isKitchenItem = kitchenItem;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        this.isAvailable = available;
    }
}