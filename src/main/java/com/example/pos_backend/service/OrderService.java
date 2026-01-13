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

        // Not: Masa durumunu burada kontrol etmiyoruz çünkü garson masaya
        // tıkladığı an masa DOLU oluyor ama sipariş henüz oluşmamış olabilir.
        // O yüzden bu kontrolü kaldırdık veya esnettik.

        Order newOrder = new Order();
        newOrder.setTable(table);
        newOrder.setWaiter(waiter);
        newOrder.setOrderStatus("BEKLEMEDE");
        newOrder.setTotalAmount(BigDecimal.ZERO);

        table.setStatus("DOLU"); // Sipariş oluşunca masa kesin dolu olsun
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

    // --- SİPARİŞ DURUMU GÜNCELLEME (GÜNCELLENDİ) ---
    @Transactional
    public Order updateOrderStatus(Long orderID, String yeniDurum) {
        Order order = getOrderById(orderID);
        order.setOrderStatus(yeniDurum);

        // SENARYO 1: Sipariş Mutfakta HAZIR oldu
        if ("HAZIR".equals(yeniDurum)) {
            if (order.getOrderItems() != null) {
                for (OrderItem item : order.getOrderItems()) {
                    item.setKitchenStatus("HAZIR");
                }
            }
        }

        // SENARYO 2: Sipariş ÖDENDİ (Kasiyer işlemi) -> MASA BOŞ OLSUN
        if ("ODENDI".equals(yeniDurum) || "KAPANDI".equals(yeniDurum)) {
            Table table = order.getTable();
            if (table != null) {
                table.setStatus("BOŞ"); // Masayı otomatik boşalt
                tableRepository.save(table);
            }
        }

        return orderRepository.save(order);
    }

    // Ürün ekleme
    @Transactional
    public Order addOrderItemToOrder(Long orderId, AddOrderItemRequest request) {

        // 1. Siparişi Bul
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Sipariş bulunamadı: " + orderId));

        // 2. Ürünü Bul
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new EntityNotFoundException("Ürün bulunamadı: " + request.getProductId()));

        // --- YENİ EKLENEN KISIM: Stok Kontrolü ---
        // Eğer istenen miktar stoktan fazlaysa hata fırlat
        if (product.getStockQuantity() < request.getQuantity()) {
            throw new RuntimeException("Yetersiz Stok! Mevcut stok: " + product.getStockQuantity());
        }

        // --- YENİ EKLENEN KISIM: Stok Düşme ---
        // Stoğu güncelle ve kaydet
        product.setStockQuantity(product.getStockQuantity() - request.getQuantity());
        productRepository.save(product);

        // 3. Sipariş Kalemi Oluşturma (Mevcut kodun devamı)
        OrderItem newItem = new OrderItem();
        newItem.setOrder(order);
        newItem.setProduct(product);
        newItem.setQuantity(request.getQuantity());
        newItem.setItemNotes(request.getItemNotes());
        newItem.setPriceAtOrder(product.getBasePrice());
        newItem.setKitchenStatus("YENI"); // Yeni eklenen ürün YENI statüsünde
        newItem.setIsServed(false);

        order.getOrderItems().add(newItem);

        // 4. Toplam Tutar Hesaplama
        BigDecimal total = order.getOrderItems().stream()
                .map(item -> item.getPriceAtOrder().multiply(new BigDecimal(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        order.setTotalAmount(total);

        return orderRepository.save(order);
    }

    @Transactional
    public Order sendOrderToKitchen(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Sipariş bulunamadı: " + orderId));

        List<OrderItem> items = order.getOrderItems();
        boolean mutfagaGidenUrunVarMi = false;

        for (OrderItem item : items) {
            // MANTIK: Eğer ürün 'YENI' (ilk kez eklendi) veya 'BEKLIYOR' durumundaysa mutfağa gönder
            if ("YENI".equals(item.getKitchenStatus()) || "BEKLIYOR".equals(item.getKitchenStatus())) {

                // Ürün mutfak ürünü mü (Yemek vb.) yoksa bar ürünü mü (İçecek vb.)?
                if (item.getProduct() != null && Boolean.TRUE.equals(item.getProduct().isKitchenItem())) {
                    // Sizin isteğiniz: Bekliyor durumundan Hazırlanıyor'a çekiyoruz
                    item.setKitchenStatus("HAZIRLANIYOR");
                    mutfagaGidenUrunVarMi = true;
                } else {
                    // İçecekler veya mutfak gerektirmeyen ürünler direkt 'HAZIR' olur
                    item.setKitchenStatus("HAZIR");
                }
            }
        }

        // Eğer mutfağa en az bir ürün gönderildiyse genel sipariş durumunu güncelle
        if (mutfagaGidenUrunVarMi) {
            order.setOrderStatus("HAZIRLANIYOR");
        }

        return orderRepository.save(order);
    }

    // --- AKTİF SİPARİŞİ GETİR (GÜVENLİ FİLTRELEME) ---
    public Order getActiveOrderByTableId(Long tableId) {
        List<Order> orders = orderRepository.findByTable_TableID(tableId);

        for (Order order : orders) {
            String status = order.getOrderStatus();

            // GÜNCELLEME: Hem boşluklu hem alt çizgili versiyonları kabul et
            if ("YENI".equals(status) ||
                    "BEKLIYOR".equals(status) ||
                    "HAZIRLANIYOR".equals(status) ||
                    "HAZIR".equals(status) ||
                    "BEKLEMEDE".equals(status) ||
                    "ÖDEME BEKLİYOR".equals(status) ||
                    "ODEME_BEKLIYOR".equals(status)) {

                return order;
            }
        }

        return null;
    }

    // Ürün silme (Stok İadeli)
    @Transactional
    public Order removeOrderItem(Long orderId, Long orderItemId) {

        // 1. Sipariş ve Ürün Doğrulaması
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Sipariş bulunamadı: " + orderId));

        OrderItem item = orderItemRepository.findById(orderItemId)
                .orElseThrow(() -> new EntityNotFoundException("Ürün bulunamadı: " + orderItemId));

        if (!item.getOrder().getOrderID()   .equals(orderId)) { // Not: getOrderID metod adın neyse onu kullan (getId, getOrderId vs.)
            throw new IllegalStateException("Bu ürün belirtilen siparişe ait değil!");
        }

        // --- YENİ EKLENEN KISIM: Stok İadesi ---
        // Silinen ürünün adedini stoğa geri ekle
        Product product = item.getProduct();
        if (product != null) {
            int quantityToRestore = item.getQuantity();
            product.setStockQuantity(product.getStockQuantity() + quantityToRestore);
            productRepository.save(product); // Güncel stoğu kaydet
        }
        // ---------------------------------------

        // 2. Ürünü Listeden ve Veritabanından Silme
        order.getOrderItems().remove(item);
        orderItemRepository.delete(item);

        // 3. Toplam Tutarın Yeniden Hesaplanması
        BigDecimal newTotal = order.getOrderItems().stream()
                .map(i -> i.getPriceAtOrder().multiply(new BigDecimal(i.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        order.setTotalAmount(newTotal);

        return orderRepository.save(order);
    }

    @Transactional
    public void deleteOrder(Long orderId) {
        // 1. Siparişi Bul
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Sipariş bulunamadı"));

        // --- YENİ EKLENEN KISIM: STOK İADESİ ---
        // Sipariş silinmeden önce içindeki ürünlerin stoklarını geri yüklüyoruz.
        if (order.getOrderItems() != null) {
            for (OrderItem item : order.getOrderItems()) {
                Product product = item.getProduct();

                // Eğer ürün veritabanında hala mevcutsa
                if (product != null) {
                    // Silinen siparişteki adet kadar stoğu artır
                    int quantityToRestore = item.getQuantity();
                    product.setStockQuantity(product.getStockQuantity() + quantityToRestore);

                    // Güncel stoğu kaydet
                    productRepository.save(product);
                }
            }
        }
        // ---------------------------------------

        // Sipariş silindiğinde masayı otomatik BOŞ YAPMIYORUZ.
        // Garson "Masayı Boşa Çıkar" diyene kadar veya yeni sipariş girene kadar DOLU kalabilir.
        // (Eğer sipariş silinince masa boşalsın istersen aşağıdaki yorumu açabilirsin)
    /*
    Table table = order.getTable();
    if (table != null) {
        table.setStatus("BOŞ");
        tableRepository.save(table);
    }
    */

        // 2. Siparişi Sil
        orderRepository.delete(order);
    }
    @Transactional
    public Order serveItem(Long orderId, Long itemId) {
        Order order = orderRepository.findById(orderId).orElseThrow();
        OrderItem item = orderItemRepository.findById(itemId).orElseThrow();

        item.setIsServed(true); // Veritabanında true yap
        item.setKitchenStatus("SERVİS EDİLDİ"); // Durumu güncelle

        return orderRepository.save(order);
    }
}