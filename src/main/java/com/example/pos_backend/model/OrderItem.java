// Paket adınız (örn: com.example.pos_backend.model)
package com.example.pos_backend.model;

import jakarta.persistence.*;
import jakarta.persistence.Table;
import com.fasterxml.jackson.annotation.JsonBackReference;
import java.math.BigDecimal;

@Entity
@Table(name = "order_items")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderItemId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    @JsonBackReference
    private Order order;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    // --- "GARSON" AKIŞI İÇİN GEREKLİ ALANLAR (Adım 104.4) ---

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false)
    private BigDecimal priceAtOrder;

    private String itemNotes; // (örn: "Acısız")

    // --- "MUTFAK" AKIŞI İÇİN YENİ EKLENEN ALANLAR (Adım 104.6) ---

    @Column(nullable = false, length = 20)
    private String kitchenStatus; // (örn: "bekliyor", "hazırlanıyor", "hazır")

    @Column(nullable = false)
    private boolean served; // (Servis edildi mi? true/false)

    // --- DÜZELTME BİTTİ ---

    // --- Constructor (Başlatıcı Metot) ---
    // (Yeni alanları varsayılan değerlerle başlatmak için)
    public OrderItem() {
        this.kitchenStatus = "bekliyor"; // Varsayılan durum
        this.served = false; // Varsayılan durum
    }

    // --- (Tüm Getter ve Setter Metotları) ---

    public Long getOrderItemId() { return orderItemId; }
    public void setOrderItemId(Long orderItemId) { this.orderItemId = orderItemId; }
    public Order getOrder() { return order; }
    public void setOrder(Order order) { this.order = order; }
    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public BigDecimal getPriceAtOrder() { return priceAtOrder; }
    public void setPriceAtOrder(BigDecimal priceAtOrder) { this.priceAtOrder = priceAtOrder; }
    public String getItemNotes() { return itemNotes; }
    public void setItemNotes(String itemNotes) { this.itemNotes = itemNotes; }

    // --- YENİ EKLENEN METOTLAR ---

    public String getKitchenStatus() { return kitchenStatus; }
    public void setKitchenStatus(String kitchenStatus) { this.kitchenStatus = kitchenStatus; }
    public boolean isServed() { return served; }
    public void setServed(boolean served) { this.served = served; }
}