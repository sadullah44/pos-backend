package com.example.pos_backend.controller; // Paket adınızın bu olduğundan emin olun

import com.example.pos_backend.dto.AddOrderItemRequest;
import com.example.pos_backend.dto.CreateOrderRequest;
import com.example.pos_backend.model.Order;
import com.example.pos_backend.service.OrderService;
import org.springframework.web.bind.annotation.*;

import java.util.List; // List importu eklendi

@CrossOrigin("*")
@RestController
@RequestMapping("/siparisler") // Ana adres
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    // --- ÖNCEKİ ENDPOINT'LER (Aynı kalıyor) ---

    /**
     * "Yeni Sipariş Oluştur" kapısı (Adım 32)
     * URL: POST /api/siparisler
     */
    @PostMapping
    public Order createNewOrder(@RequestBody CreateOrderRequest request) {
        return orderService.createOrder(request.getTableId(), request.getWaiterId());
    }

    /**
     * "Ürün Ekle" kapısı (Adım 36 - 'urunEkle' sizin seçiminizdi)
     * URL: POST /api/siparisler/{orderID}/urunEkle
     */
    @PostMapping("/{orderID}/urunEkle")
    public Order addOrderItemToOrder(
            @PathVariable Long orderID,
            @RequestBody AddOrderItemRequest request
    ) {
        return orderService.addOrderItemToOrder(
                orderID,
                request.getProductID(),
                request.getQuantity(),
                request.getItemNotes()
        );
    }

    // --- YENİ EKLENEN ENDPOINT'LER (Adım 44) ---

    /**
     * YENİ ENDPOINT (Android için): Tüm siparişleri getirir.
     * URL: GET http://localhost:8080/api/siparisler
     */
    @GetMapping
    public List<Order> getAllOrders() {
        return orderService.getAllOrders();
    }

    /**
     * YENİ ENDPOINT (Android için): Tek bir siparişin detaylarını getirir.
     * URL: GET http://localhost:8080/api/siparisler/3
     */
    @GetMapping("/{orderID}")
    public Order getOrderById(@PathVariable Long orderID) {
        return orderService.getOrderById(orderID);
    }

    /**
     * YENİ ENDPOINT (SENARYO 3: "Sipariş Durumunu Güncelle")
     * URL: PUT http://localhost:8080/api/siparisler/3/durum?yeniDurum=tamamlandi
     */
    @PutMapping("/{orderID}/durum")
    public Order updateOrderStatus(
            @PathVariable Long orderID,                 // @PathVariable: URL'deki {orderID} kısmını (örn: 3) alır.
            @RequestParam String yeniDurum              // @RequestParam: URL'deki ?yeniDurum=... kısmını (örn: "tamamlandi") alır.
    ) {
        // KAPI (Controller), İŞİ BEYNE (Service) PASLAR
        // "Beyne diyoruz ki: 3 numaralı siparişin durumunu 'tamamlandi' olarak güncelle."
        return orderService.updateOrderStatus(orderID, yeniDurum);
    }
}