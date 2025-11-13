// Paket adınız (örn: com.example.pos_backend.model)
package com.example.pos_backend.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList; // <-- KRİTİK İMPORT
import java.util.List; // <-- KRİTİK İMPORT
import com.fasterxml.jackson.annotation.JsonManagedReference;
@Entity
@jakarta.persistence.Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderID; // Sizin 'Büyük D' isimlendirmeniz

    @ManyToOne
    @JoinColumn(name = "table_id", nullable = false)
    private Table table;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User waiter;

    private String orderStatus;
    private BigDecimal totalAmount;

    /**
     * "Sepet Kalemi" (OrderItem) listesi
     * 'CascadeType.ALL': Bu 'Order' (Sepet) kaydedildiğinde,
     * içindeki TÜM 'orderItems' (Kalemler) de otomatik kaydedilir.
     */
    @OneToMany(
            mappedBy = "order",
            cascade = CascadeType.ALL, // KRİTİK
            orphanRemoval = true,
            fetch = FetchType.EAGER
    )
    @JsonManagedReference // <-- 2. "SAHİP" ("Sepet") TERCÜMANINI EKLE
    private List<OrderItem> orderItems;

    /**
     * --- DÜZELTME (Adım 105.D - "Boş Liste Hatası" Çözümü) ---
     * Kurucu Metot (Constructor)
     * Yeni bir 'Order' (Sepet) oluşturulduğunda, 'orderItems' listesini
     * 'null' (boş) bırakmak yerine, onu 'boş bir liste' olarak BAŞLAT.
     *
     * Bu, 'order.getOrderItems().add(...)' komutunun
     * 'NullPointerException' (Sunucu Çökmesi) vermesini ENGELLER.
     */
    public Order() {
        this.orderItems = new ArrayList<>();
    }
    // --- DÜZELTME BİTTİ ---


    // --- (Tüm Getter ve Setter Metotları) ---
    // (orderID, table, waiter, orderStatus, totalAmount, orderItems)

    public Long getOrderID() { return orderID; }
    public void setOrderID(Long orderID) { this.orderID = orderID; }
    public Table getTable() { return table; }
    public void setTable(Table table) { this.table = table; }
    public User getWaiter() { return waiter; }
    public void setWaiter(User waiter) { this.waiter = waiter; }
    public String getOrderStatus() { return orderStatus; }
    public void setOrderStatus(String orderStatus) { this.orderStatus = orderStatus; }
    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }
    public List<OrderItem> getOrderItems() { return orderItems; }
    public void setOrderItems(List<OrderItem> orderItems) { this.orderItems = orderItems; }
}