package com.example.pos_backend.dto; // 'dto' paketimiz
import java.math.BigDecimal;

/**
 * Bu sınıf bir @Entity DEĞİLDİR.
 * Sadece 'POST /api/odemeler' (veya seçeceğimiz başka bir URL) endpoint'ine gönderilen
 * JSON'u yakalamak için kullanılan bir Veri Taşıma Nesnesidir (DTO).
 */
public class CreatePaymentRequest {

    private Long orderID;
    private Long paymentMethodID;
    private Long cashierID;
    private BigDecimal amountPaid;

    // JSON'u nesneye çevirmek (Deserialization) için
    // Spring'in (Jackson kütüphanesi) bu getter ve setter'lara ihtiyacı var.

    public Long getOrderID() {
        return orderID;
    }

    public void setOrderID(Long orderID) {
        this.orderID = orderID;
    }

    public Long getPaymentMethodID() {
        return paymentMethodID;
    }

    public void setPaymentMethodID(Long paymentMethodID) {
        this.paymentMethodID = paymentMethodID;
    }

    public Long getCashierID() {
        return cashierID;
    }

    public void setCashierID(Long cashierID) {
        this.cashierID = cashierID;
    }

    public BigDecimal getAmountPaid() {
        return amountPaid;
    }

    public void setAmountPaid(BigDecimal amountPaid) {
        this.amountPaid = amountPaid;
    }
}