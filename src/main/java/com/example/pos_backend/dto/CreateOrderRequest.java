package com.example.pos_backend.dto; // Yeni 'dto' paketimiz

/**
 * Bu sınıf bir @Entity DEĞİLDİR.
 * Sadece 'POST /api/siparisler' endpoint'ine gönderilen JSON'u
 * yakalamak için kullanılan bir Veri Taşıma Nesnesidir (DTO).
 */
public class CreateOrderRequest {

    private Long tableID;
    private Long waiterID;

    // JSON'u nesneye çevirmek (Deserialization) için
    // Spring'in (Jackson kütüphanesi) bu getter ve setter'lara ihtiyacı var.

    public Long getTableID() {
        return tableID;
    }

    public void setTableID(Long tableID) {
        this.tableID = tableID;
    }

    public Long getWaiterID() {
        return waiterID;
    }

    public void setWaiterID(Long waiterID) {
        this.waiterID = waiterID;
    }
}