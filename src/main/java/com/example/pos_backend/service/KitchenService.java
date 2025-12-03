package com.example.pos_backend.service;

import com.example.pos_backend.model.Order;
import com.example.pos_backend.model.OrderItem;
import com.example.pos_backend.repository.OrderItemRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class KitchenService {

    private final OrderItemRepository orderItemRepository;

    public KitchenService(OrderItemRepository orderItemRepository) {
        this.orderItemRepository = orderItemRepository;
    }

    /**
     * Mutfakta görülecek ürünler:
     * BEKLIYOR + HAZIRLANIYOR
     */
    public List<OrderItem> getKitchenItems() {

        // 1) Durumu BEKLIYOR veya HAZIRLANIYOR olanlar
        List<OrderItem> items = orderItemRepository.findByKitchenStatusIn(
                List.of("BEKLIYOR", "HAZIRLANIYOR")
        );

        // 2) Sadece mutfak ürünleri (kitchenItem = true) gelsin
        return items.stream()
                .filter(item -> item.getProduct() != null && item.getProduct().isKitchenItem())
                .toList();
    }


    /**
     * Tek ürünü hazır yap
     */
    public OrderItem setItemReady(Long itemId) {

        OrderItem item = orderItemRepository.findById(itemId)
                .orElseThrow(() -> new EntityNotFoundException("Ürün bulunamadı: " + itemId));

        // 1) Bu ürün hazır yapılıyor
        item.setKitchenStatus("HAZIR");
        orderItemRepository.save(item);

        // 2) Sipariş artık tamamen hazır mı kontrol et
        Order order = item.getOrder();
        boolean allReady = order.getOrderItems()
                .stream()
                .allMatch(i -> i.getKitchenStatus().equals("HAZIR"));

        // 3) Hepsi hazırsa siparişi HAZIR yap
        if (allReady) {
            order.setOrderStatus("HAZIR");
        } else {
            order.setOrderStatus("HAZIRLANIYOR");
        }

        return item;
    }


    /**
     * Ürünü hazırlanmaya al (opsiyonel)
     */
    public OrderItem startPreparing(Long itemId) {
        OrderItem item = orderItemRepository.findById(itemId)
                .orElseThrow(() -> new EntityNotFoundException("Ürün bulunamadı: " + itemId));

        item.setKitchenStatus("HAZIRLANIYOR");
        return orderItemRepository.save(item);
    }
}
