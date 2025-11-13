package com.example.pos_backend.dto; // 'dto' paketimiz

/**
 * Bu sınıf bir @Entity DEĞİLDİR.
 * Sadece 'POST /api/siparisler/{id}/kalemEkle' endpoint'ine gönderilen
 * JSON'u yakalamak için kullanılan bir Veri Taşıma Nesnesidir (DTO).
 */
public class AddOrderItemRequest {

    private Long productId;
    private int quantity;
    private String itemNotes;

    // JSON'u nesneye çevirmek (Deserialization) için
    // Spring'in (Jackson kütüphanesi) bu getter ve setter'lara ihtiyacı var.

    public Long getProductId() {
        return productId;
    }

    public void setProductID(Long productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getItemNotes() {
        return itemNotes;
    }

    public void setItemNotes(String itemNotes) {
        this.itemNotes = itemNotes;
    }
}