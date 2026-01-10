package com.example.pos_backend.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@jakarta.persistence.Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderID;

    // --- Normal Alanlar ---
    @Column(name = "created_at", updatable = false, columnDefinition = "TIMESTAMP(0)")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    private String orderStatus;
    private BigDecimal totalAmount;

    // --- İlişkiler ---
    @ManyToOne
    @JoinColumn(name = "table_id", nullable = false)
    private Table table;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User waiter;

    @OneToMany(
            mappedBy = "order",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.EAGER // Sipariş kalemlerini getirir
    )
    @JsonManagedReference
    private List<OrderItem> orderItems;

    // --- EKLENEN KISIM: Payments İlişkisi ---
    // Bu kısım eksikti. Bu yüzden ödeme verisi gelmiyordu.
    @OneToMany(
            mappedBy = "order",         // Payment sınıfındaki "private Order order;" ile eşleşir
            cascade = CascadeType.ALL,
            fetch = FetchType.EAGER     // ÖNEMLİ: Siparişi çekerken ödemeyi de YÜKLE
    )
    @JsonManagedReference // Sonsuz döngüyü engeller (Payment tarafında @JsonBackReference olmalı)
    private List<Payment> payments = new ArrayList<>();
    // ----------------------------------------

    // Sipariş oluşturulurken tarihi otomatik ata
    @PrePersist
    protected void onCreate() {
        // Nanosaniyeyi sıfırlayarak temiz tarih formatı sağlar
        this.createdAt = LocalDateTime.now().withNano(0);
    }

    public Order() {
        this.orderItems = new ArrayList<>();
        this.payments = new ArrayList<>(); // Null hatası almamak için listeyi başlatıyoruz
    }

    // --- Getter ve Setterlar ---

    public Long getOrderID() { return orderID; }
    public void setOrderID(Long orderID) { this.orderID = orderID; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

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

    // --- EKLENEN Getter ve Setter ---
    public List<Payment> getPayments() { return payments; }
    public void setPayments(List<Payment> payments) { this.payments = payments; }
}