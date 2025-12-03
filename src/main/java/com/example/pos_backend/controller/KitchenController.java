package com.example.pos_backend.controller;

import com.example.pos_backend.model.OrderItem;
import com.example.pos_backend.service.KitchenService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/mutfak")
public class KitchenController {

    private final KitchenService kitchenService;

    public KitchenController(KitchenService kitchenService) {
        this.kitchenService = kitchenService;
    }

    // Mutfakta görülecek ürünler
    @GetMapping("/urunler")
    public List<OrderItem> getKitchenItems() {
        return kitchenService.getKitchenItems();
    }

    // Ürünü HAZIR yap
    @PutMapping("/urun/{itemId}/hazir")
    public OrderItem setItemReady(@PathVariable Long itemId) {
        return kitchenService.setItemReady(itemId);
    }

    // Ürünü HAZIRLANIYOR yap (opsiyonel buton için)
    @PutMapping("/urun/{itemId}/hazirlanıyor")
    public OrderItem startPreparing(@PathVariable Long itemId) {
        return kitchenService.startPreparing(itemId);
    }
}
