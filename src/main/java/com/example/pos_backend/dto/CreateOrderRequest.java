// Paket adınız (örn: com.example.pos_backend.dto)
package com.example.pos_backend.dto;

/**
 * Bu DTO (Veri Aktarım Nesnesi), Android'den
 * '/siparisler' (Yeni Sipariş Oluştur) kapısına gelen
 * JSON'u yakalamak için kullanılır.
 *
 * --- GÜNCELLEME (Adım 88): ---
 * 'tableID' (Büyük D) -> 'tableId' (Küçük d) olarak düzeltildi.
 * Bu, Java'nın 'camelCase' isimlendirme standardıdır ve
 * Android'den (GSON) gelen {"tableId":...} JSON'u ile
 * artık BİREBİR EŞLEŞECEKTİR.
 */
public class CreateOrderRequest {

    // Alan adları, Android'in (GSON) gönderdiği JSON "key"leri ile
    // BİREBİR AYNI (Büyük/Küçük harf duyarlı) olmalıdır.
    private Long tableId;  // <-- DÜZELTİLDİ (küçük 'd')
    private Long waiterId; // <-- DÜZELTİLDİ (küçük 'd')

    // Jackson'ın (veya diğer kütüphanelerin)
    // 'new CreateOrderRequest()' yapabilmesi için boş bir kurucu (constructor)
    public CreateOrderRequest() {
    }

    // --- GETTER METOTLARI ---
    // (Java standartlarına göre 'getTableId' olmalı)

    public Long getTableId() {
        return tableId;
    }

    public Long getWaiterId() {
        return waiterId;
    }

    // --- SETTER METOTLARI ---
    // (Java standartlarına göre 'setTableId' olmalı)
    // Jackson, {"tableId":1} JSON'unu görünce bu 'setTableId' metodunu çağıracak.

    public void setTableId(Long tableId) {
        this.tableId = tableId;
    }

    public void setWaiterId(Long waiterId) {
        this.waiterId = waiterId;
    }
}