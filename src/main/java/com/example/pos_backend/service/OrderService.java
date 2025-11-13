// Paket adınız (örn: com.example.pos_backend.service)
package com.example.pos_backend.service;

import com.example.pos_backend.dto.AddOrderItemRequest;
import com.example.pos_backend.dto.CreateOrderRequest;
import com.example.pos_backend.model.*; // (Order, OrderItem, Product, Table, User)
import com.example.pos_backend.repository.*; // (Gerekli tüm Repository'ler)
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional // Bu sınıftaki metotların 'ya hep ya hiç' (bütüncül) çalışmasını sağlar
public class OrderService {

    // --- "MÜTEAHHİT" (REPOSITORY) LİSTESİ ---

    private final OrderRepository orderRepository;
    private final TableRepository tableRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final OrderItemRepository orderItemRepository;

    /**
     * Kurucu Metot (Constructor)
     * Gerekli tüm "Müteahhit"ler (Repository) buraya 'inject' (dahil) edilir
     */
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

    // --- MEVCUT METOTLAR (TAM KODLARIYLA) ---

    /**
     * "Yeni Sipariş Oluştur" Mantığı (Adım 90'dan)
     */
    @Transactional
    public Order createOrder(Long tableID, Long waiterID) {

        Table table = tableRepository.findById(tableID)
                .orElseThrow(() -> new EntityNotFoundException("Masa bulunamadı, ID: " + tableID));

        User waiter = userRepository.findById(waiterID)
                .orElseThrow(() -> new EntityNotFoundException("Garson (User) bulunamadı, ID: " + waiterID));

        // 'STATUS_AVAILABLE' ("Boş") sizin 'Table' modelinizde tanımlı olmalı
        if (!table.getStatus().equals(Table.STATUS_AVAILABLE)) {
            throw new IllegalStateException("Masa '" + table.getTableName() + "' şu anda 'Boş' değil, yeni sipariş açılamaz.");
        }

        Order newOrder = new Order();
        newOrder.setTable(table);
        newOrder.setWaiter(waiter);
        newOrder.setOrderStatus("beklemede"); // Sizin 'beklemede' durumunuzu kullanıyoruz
        newOrder.setTotalAmount(BigDecimal.ZERO);
        // newOrder.setOrderItems(new java.util.ArrayList<>()); // Liste boş başlar

        // 'STATUS_OCCUPIED' ("Dolu") sizin 'Table' modelinizde tanımlı olmalı
        table.setStatus(Table.STATUS_OCCUPIED);
        tableRepository.save(table);

        // 'save' metodunun DÖNDÜRDÜĞÜ (ID'si dolu) nesneyi 'return' et
        return orderRepository.save(newOrder);
    }

    /**
     * "Tüm Siparişleri Getir" Mantığı
     */
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    /**
     * "ID'ye Göre Sipariş Getir" Mantığı
     */
    public Order getOrderById(Long orderID) {
        return orderRepository.findById(orderID)
                .orElseThrow(() -> new EntityNotFoundException("Sipariş bulunamadı, ID: " + orderID));
    }

    /**
     * "Sipariş Durumunu Güncelle" Mantığı
     */
    @Transactional
    public Order updateOrderStatus(Long orderID, String yeniDurum) {
        // 'getOrderById' metodunu yeniden kullanarak siparişi bul
        Order order = getOrderById(orderID);
        order.setOrderStatus(yeniDurum);
        return orderRepository.save(order);
    }


    // --- YENİ EKLENEN METOT (Adım 104.3) ---
    /**
     * "Mutfağa Ürün Ekleme" Akışı Adım 10 (Final):
     * 'OrderController'dan (Kapı) gelen isteği yerine getirir.
     * Bu, "Mutfağa ürün ekleme" akışının GERÇEK BEYİN mantığıdır.
     */
    @Transactional // Bu metodun 'ya hep ya hiç' (atomik) çalışması KRİTİKTİR
    public Order addOrderItemToOrder(Long orderId, AddOrderItemRequest request) {

        // Adım 1: "Sepet"i (Order) Bul
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Sipariş bulunamadı, ID: " + orderId));

        // Adım 2: "Ürün"ü (Product) Bul
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new EntityNotFoundException("Ürün bulunamadı, ID: " + request.getProductId()));

        // Adım 3: Yeni "Sepet Kalemi"ni (OrderItem) Oluştur
        OrderItem newOrderItem = new OrderItem();
        newOrderItem.setOrder(order); // Bu kalemin hangi "Sepet"e ait olduğunu ayarla
        newOrderItem.setProduct(product); // Bu kalemin hangi "Ürün" olduğunu ayarla
        newOrderItem.setQuantity(request.getQuantity()); // Adedi ayarla
        newOrderItem.setItemNotes(request.getItemNotes()); // Notları ayarla

        // Fiyatı, "Ürün"ün O ANKİ fiyatından kopyala (Çok Önemli)
        newOrderItem.setPriceAtOrder(product.getBasePrice());

        // Adım 4: "Sepet Kalemi"ni "Sepet"e Ekle
        // (Eğer 'Order' modelinizde '@OneToMany(cascade = CascadeType.ALL)'
        //  ayarı varsa, bu 'add' işlemi, 'orderRepository.save(order)'
        //  çağrıldığında 'newOrderItem'ı otomatik olarak veritabanına ekler)
        order.getOrderItems().add(newOrderItem);

        // Adım 5: "Sepet"in Toplam Tutarını YENİDEN HESAPLA
        BigDecimal newTotal = order.getOrderItems().stream()
                .map(item -> item.getPriceAtOrder().multiply(new BigDecimal(item.getQuantity())))
                .reduce(BigDecimal.ZERO, (total, price) -> total.add(price));

        order.setTotalAmount(newTotal);

        // Adım 6: Güncellenmiş "Sepet"i (Order) Kaydet
        Order savedOrder = orderRepository.save(order);

        // Adım 7: Güncellenmiş "Sepet"i (Order) Android'e Geri Döndür
        return savedOrder;
    }
    // --- DÜZELTME BİTTİ ---
}