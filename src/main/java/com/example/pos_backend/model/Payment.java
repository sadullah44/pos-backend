package com.example.pos_backend.model;

import com.fasterxml.jackson.annotation.JsonBackReference; // Bu import'u ekleyin
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentID;

    @ManyToOne
    @JoinColumn(name = "orderID", nullable = false)
    @JsonBackReference // ÖNEMLİ: Order içinden Payment çekilirken döngüye girmemesi için
    private Order order;

    @ManyToOne
    @JoinColumn(name = "paymentMethodID", nullable = false)
    private PaymentMethod paymentMethod;

    @ManyToOne
    @JoinColumn(name = "cashierID", nullable = false)
    private User cashier;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amountPaid;

    @Column(name = "payment_time", updatable = false, columnDefinition = "TIMESTAMP(0)")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime paymentTime;

    public Payment() {
    }

    @PrePersist
    protected void onPayment() {
        // Hem veritabanı (TIMESTAMP(0)) hem Java (withNano(0)) seviyesinde saliseleri siliyoruz
        this.paymentTime = LocalDateTime.now().withNano(0);
    }

    // --- Getter ve Setter Metotları ---
    public Long getPaymentID() { return paymentID; }
    public void setPaymentID(Long paymentID) { this.paymentID = paymentID; }

    public Order getOrder() { return order; }
    public void setOrder(Order order) { this.order = order; }

    public PaymentMethod getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(PaymentMethod paymentMethod) { this.paymentMethod = paymentMethod; }

    public User getCashier() { return cashier; }
    public void setCashier(User cashier) { this.cashier = cashier; }

    public BigDecimal getAmountPaid() { return amountPaid; }
    public void setAmountPaid(BigDecimal amountPaid) { this.amountPaid = amountPaid; }

    public LocalDateTime getPaymentTime() { return paymentTime; }
    public void setPaymentTime(LocalDateTime paymentTime) { this.paymentTime = paymentTime; }
}