package com.example.pos_backend.service;

import com.example.pos_backend.model.*;
import com.example.pos_backend.repository.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal; // Bu import'u ekleyin
import java.util.List;

@Service
public class OrderService {

    // ... (Constructor ve Repository alanları aynı, Adım 34'teki gibi 5 repository'li)
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
                .orElseThrow(() -> new EntityNotFoundException("Masa bulunamadı, ID: " + tableID));

        User waiter = userRepository.findById(waiterID)
                .orElseThrow(() -> new EntityNotFoundException("Garson (User) bulunamadı, ID: " + waiterID));

        if (!table.getStatus().equals(Table.STATUS_AVAILABLE)) {
            throw new IllegalStateException("Masa '" + table.getTableName() + "' şu anda 'Boş' değil, yeni sipariş açılamaz.");
        }

        Order newOrder = new Order();
        newOrder.setTable(table);
        newOrder.setWaiter(waiter);
        newOrder.setOrderStatus("beklemede");

        // --- DEĞİŞİKLİK 1: Para Tipi ---
        // '0.0' (double) yerine, 'BigDecimal' tipinde '0'
        newOrder.setTotalAmount(BigDecimal.ZERO);

        Order savedOrder = orderRepository.save(newOrder);

        table.setStatus(Table.STATUS_OCCUPIED);
        tableRepository.save(table);

        return savedOrder;
    }


    @Transactional
    public Order addOrderItemToOrder(Long orderID, Long productID, int quantity, String itemNotes) {

        Order order = orderRepository.findById(orderID)
                .orElseThrow(() -> new EntityNotFoundException("Sipariş bulunamadı, ID: " + orderID));

        Product product = productRepository.findById(productID)
                .orElseThrow(() -> new EntityNotFoundException("Ürün bulunamadı, ID: " + productID));

        OrderItem newOrderItem = new OrderItem();
        newOrderItem.setOrder(order);
        newOrderItem.setProduct(product);
        newOrderItem.setQuantity(quantity);
        newOrderItem.setItemNotes(itemNotes);
        newOrderItem.setKitchenStatus("beklemede");
        newOrderItem.setServed(false);

        // KURAL: 'priceAtOrder' (BigDecimal) alanını üründeki 'basePrice' (BigDecimal) alanından al
        newOrderItem.setPriceAtOrder(product.getBasePrice());

        orderItemRepository.save(newOrderItem);

        // --- DEĞİŞİKLİK 2: Toplam Hesaplama Mantığı ---
        // O siparişe ait TÜM kalemleri (yeni eklenen dahil) veritabanından bul.
        List<OrderItem> allItemsForThisOrder = orderItemRepository.findByOrder(order);

        // 'double newTotalAmount = 0.0;' yerine 'BigDecimal' tipinde '0'
        BigDecimal newTotalAmount = BigDecimal.ZERO;

        // Toplam tutarı 'BigDecimal' matematiği ile yeniden hesapla
        for (OrderItem item : allItemsForThisOrder) {

            // 1. Kalemin Fiyatını Al (BigDecimal)
            BigDecimal itemPrice = item.getPriceAtOrder();

            // 2. Kalemin Adedini Al (int) ve BigDecimal'e çevir
            BigDecimal itemQuantity = new BigDecimal(item.getQuantity());

            // 3. Çarpma: itemTotal = itemPrice * itemQuantity
            BigDecimal itemTotal = itemPrice.multiply(itemQuantity);

            // 4. Toplama: newTotalAmount = newTotalAmount + itemTotal
            newTotalAmount = newTotalAmount.add(itemTotal);
        }

        // Ana 'Order' (Sipariş) kaydının toplam tutarını (BigDecimal) güncelle ve kaydet.
        order.setTotalAmount(newTotalAmount);
        return orderRepository.save(order);
    }

    // --- (getAllOrders, getOrderById, updateOrderStatus metotları aynı kalır) ---
    // (Aşağıdaki metotlar Adım 43'te eklendiği gibi kalmalı)

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Order getOrderById(Long orderID) {
        return orderRepository.findById(orderID)
                .orElseThrow(() -> new EntityNotFoundException("Sipariş bulunamadı, ID: " + orderID));
    }

    @Transactional
    public Order updateOrderStatus(Long orderID, String newStatus) {

        Order orderToUpdate = orderRepository.findById(orderID)
                .orElseThrow(() -> new EntityNotFoundException("Sipariş bulunamadı, ID: " + orderID));

        orderToUpdate.setOrderStatus(newStatus);

        return orderRepository.save(orderToUpdate);
    }
}