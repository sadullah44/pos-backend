package com.example.pos_backend.service;

import com.example.pos_backend.dto.AddOrderItemRequest;
import com.example.pos_backend.model.*;
import com.example.pos_backend.repository.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final TableRepository tableRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final OrderItemRepository orderItemRepository;

    public OrderService(OrderRepository orderRepository,
                        TableRepository tableRepository,
                        UserRepository userRepository,
                        ProductRepository productRepository,
                        OrderItemRepository orderItemRepository) {

        this.orderRepository = orderRepository;
        this.tableRepository = tableRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.orderItemRepository = orderItemRepository;
    }

    @Transactional
    public Order createOrder(Long tableID, Long waiterID) {

        Table table = tableRepository.findById(tableID)
                .orElseThrow(() -> new EntityNotFoundException("Masa bulunamadı: " + tableID));

        User waiter = userRepository.findById(waiterID)
                .orElseThrow(() -> new EntityNotFoundException("Garson bulunamadı: " + waiterID));

        if (!table.getStatus().equals(Table.STATUS_AVAILABLE)) {
            throw new IllegalStateException("Bu masada zaten aktif sipariş var.");
        }

        Order newOrder = new Order();
        newOrder.setTable(table);
        newOrder.setWaiter(waiter);
        newOrder.setOrderStatus("BEKLEMEDE");
        newOrder.setTotalAmount(BigDecimal.ZERO);

        table.setStatus(Table.STATUS_OCCUPIED);
        tableRepository.save(table);

        return orderRepository.save(newOrder);
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Order getOrderById(Long orderID) {
        return orderRepository.findById(orderID)
                .orElseThrow(() -> new EntityNotFoundException("Sipariş bulunamadı: " + orderID));
    }

    @Transactional
    public Order updateOrderStatus(Long orderID, String yeniDurum) {
        Order order = getOrderById(orderID);
        order.setOrderStatus(yeniDurum);

        // --- EKLENEN KISIM BAŞLANGIÇ ---
        // Eğer siparişin genel durumu "HAZIR" yapıldıysa,
        // içindeki tüm ürünlerin durumunu da "HAZIR" yap.
        // Böylece garson yeni ürün eklediğinde eskiler tekrar mutfağa düşmez.
        if ("HAZIR".equals(yeniDurum)) {
            if (order.getOrderItems() != null) {
                for (OrderItem item : order.getOrderItems()) {
                    item.setKitchenStatus("HAZIR");
                }
            }
        }
        // --- EKLENEN KISIM BİTİŞ ---

        return orderRepository.save(order);
    }

    // 1. MEVCUT METODU GÜNCELLE: Ürün eklenince 'BEKLIYOR' değil 'YENI' olsun.
    @Transactional
    public Order addOrderItemToOrder(Long orderId, AddOrderItemRequest request) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Sipariş bulunamadı: " + orderId));

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new EntityNotFoundException("Ürün bulunamadı: " + request.getProductId()));

        OrderItem newItem = new OrderItem();
        newItem.setOrder(order);
        newItem.setProduct(product);
        newItem.setQuantity(request.getQuantity());
        newItem.setItemNotes(request.getItemNotes());
        newItem.setPriceAtOrder(product.getBasePrice());

        // --- DEĞİŞİKLİK BURADA ---
        // Eskiden: newItem.setKitchenStatus("BEKLIYOR");
        // Yeni:
        newItem.setKitchenStatus("YENI");
        // -------------------------

        newItem.setIsServed(false);

        order.getOrderItems().add(newItem);

        // Fiyat hesaplama mantığın aynı kalıyor
        BigDecimal total = order.getOrderItems().stream()
                .map(item -> item.getPriceAtOrder().multiply(new BigDecimal(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        order.setTotalAmount(total);

        return orderRepository.save(order);
    }
    // 2. YENİ METOD EKLE: Sadece yeni eklenenleri mutfağa bildir
    @Transactional
    public Order sendOrderToKitchen(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Sipariş bulunamadı: " + orderId));

        List<OrderItem> items = order.getOrderItems();
        boolean mutfagaGidenUrunVarMi = false;

        for (OrderItem item : items) {
            // Sadece yeni eklenen ürünleri işle
            if ("YENI".equals(item.getKitchenStatus())) {

                // --- KONTROL BURADA ---
                // Ürünün 'isKitchenItem' (mutfak ürünü mü?) özelliğine bakıyoruz.
                // Product modelinde bu alanın getter metodunun adı 'isKitchenItem' veya 'getKitchenItem' olabilir.
                // Genellikle boolean olduğu için 'isKitchenItem()' kullanılır.

                if (item.getProduct() != null && Boolean.TRUE.equals(item.getProduct().isKitchenItem())) {

                    // Eğer Yemek ise -> Mutfağa gönder (BEKLIYOR)
                    item.setKitchenStatus("BEKLIYOR");
                    mutfagaGidenUrunVarMi = true;

                } else {

                    // Eğer İçecek ise -> Direkt HAZIR yap.
                    // Böylece Android tarafındaki filtremiz (HAZIR olanları gizle) sayesinde ekranda görünmez.
                    item.setKitchenStatus("HAZIR");
                }
            }
        }

        // Eğer mutfağa en az bir yemek gittiyse sipariş durumu 'HAZIRLANIYOR' olsun.
        // Sadece içecek sipariş edildiyse sipariş durumu değişmesin veya direkt 'HAZIR' olsun.
        if (mutfagaGidenUrunVarMi) {
            order.setOrderStatus("HAZIRLANIYOR");
        } else {
            // Opsiyonel: Eğer sadece içecek varsa ve hepsi hazırlandıysa siparişi de HAZIR yapabilirsin.
            // order.setOrderStatus("HAZIR");
        }

        return orderRepository.save(order);
    }

    public Order getActiveOrderByTableId(Long tableId) {
        return orderRepository.findFirstByTable_TableIDAndOrderStatusNot(tableId, "ÖDENDİ")
                .orElse(null);
    }
    @Transactional
    public Order removeOrderItem(Long orderId, Long orderItemId) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Sipariş bulunamadı: " + orderId));

        OrderItem item = orderItemRepository.findById(orderItemId)
                .orElseThrow(() -> new EntityNotFoundException("Ürün bulunamadı: " + orderItemId));

        // Siparişe ait değilse engelle
        if (!item.getOrder().getOrderID().equals(orderId)) {
            throw new IllegalStateException("Bu ürün belirtilen siparişe ait değil!");
        }

        // 1) Ürünü sipariş listesinden çıkar
        order.getOrderItems().remove(item);

        // 2) Ürünü fiziksel olarak DB'den sil
        orderItemRepository.delete(item);

        // 3) Toplam tutarı yeniden hesapla
        BigDecimal newTotal = order.getOrderItems().stream()
                .map(i -> i.getPriceAtOrder().multiply(new BigDecimal(i.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        order.setTotalAmount(newTotal);

        return orderRepository.save(order);
    }

}
