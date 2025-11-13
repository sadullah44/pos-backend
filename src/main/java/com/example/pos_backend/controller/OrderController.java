// Paket adınız (örn: com.example.pos_backend.controller)
package com.example.pos_backend.controller;

import com.example.pos_backend.dto.AddOrderItemRequest; // <-- SADECE 1 KEZ İMPORT EDİLDİ
import com.example.pos_backend.dto.CreateOrderRequest;
import com.example.pos_backend.model.Order;
import com.example.pos_backend.service.OrderService;
import org.springframework.web.bind.annotation.*;

import java.util.List; // List importu

@CrossOrigin("*")
@RestController
@RequestMapping("/siparisler") // Ana adres
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    // --- MEVCUT KAPILAR (Bunlar doğruydu) ---

    /**
     * "Yeni Sipariş Oluştur" kapısı
     * Adres: POST /siparisler
     */
    @PostMapping
    public Order createNewOrder(@RequestBody CreateOrderRequest request) {
        // (Bu metot Adım 104.3'teki 'createOrder' (Beyin) ile eşleşir)
        return orderService.createOrder(request.getTableId(), request.getWaiterId());
    }

    /**
     * "Tüm Siparişleri Getir" kapısı
     * Adres: GET /siparisler
     */
    @GetMapping
    public List<Order> getAllOrders() {
        return orderService.getAllOrders();
    }

    /**
     * "ID'ye Göre Sipariş Getir" kapısı
     * Adres: GET /siparisler/{orderID}
     */
    @GetMapping("/{orderID}")
    public Order getOrderById(@PathVariable Long orderID) {
        return orderService.getOrderById(orderID);
    }

    /**
     * "Sipariş Durumunu Güncelle" kapısı
     * Adres: PUT /siparisler/{orderID}/durum
     */
    @PutMapping("/{orderID}/durum")
    public Order updateOrderStatus(
            @PathVariable Long orderID,
            @RequestParam String yeniDurum
    ) {
        return orderService.updateOrderStatus(orderID, yeniDurum);
    }

    // --- "ESKİ" (Adım 36) METOT SİLİNDİ ---
    // (O 'orderService.addOrderItemToOrder(orderID, request.getProductId(), ...)'
    // metodunu çağıran 'kırmızı hata'lı kod buradan kaldırıldı)
    // --- TEMİZLİK BİTTİ ---


    // --- "YENİ" (Adım 104.2) METOT (Bu kalıyor) ---
    /**
     * "Mutfağa Ürün Ekleme" Akışı Adım 10 (Final):
     * Mevcut bir siparişe yeni bir ürün kalemi (örn: "Sütlaç") ekler.
     * Adres: POST /siparisler/{orderId}/urunEkle
     */
    @PostMapping("/{orderId}/urunEkle")
    public Order addOrderItemToOrder(
            @PathVariable Long orderId,
            @RequestBody AddOrderItemRequest request // (Adım 104.1 DTO'su)
    ) {
        // (Bu metot Adım 104.3'teki 'addOrderItemToOrder(Long, AddOrderItemRequest)'
        // (Beyin) metoduyla EŞLEŞİR)
        return orderService.addOrderItemToOrder(orderId, request);
    }
    // --- DÜZELTME BİTTİ ---
}