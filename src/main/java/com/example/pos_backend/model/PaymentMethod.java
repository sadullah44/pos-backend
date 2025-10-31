package com.example.pos_backend.model; // Paket adınızın bu olduğundan emin olun

import jakarta.persistence.*;
import jakarta.persistence.Table;

@Entity
@Table(name = "payment_methods") // Veritabanı tablo adı
public class PaymentMethod {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentMethodID; // Sütun adı: paymentMethodID

    @Column(nullable = false, unique = true) // Ödeme yöntemleri benzersiz olmalı (örn: 2 tane "Nakit" olamaz)
    private String methodName; // Sütun adı: methodName (örn: "Nakit", "Kredi Kartı")

    // JPA için boş constructor
    public PaymentMethod() {
    }

    // Test verisi eklerken kullanmak için bir constructor
    public PaymentMethod(String methodName) {
        this.methodName = methodName;
    }

    // --- Getter ve Setter Metotları ---

    public Long getPaymentMethodID() {
        return paymentMethodID;
    }

    public void setPaymentMethodID(Long paymentMethodID) {
        this.paymentMethodID = paymentMethodID;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }
}