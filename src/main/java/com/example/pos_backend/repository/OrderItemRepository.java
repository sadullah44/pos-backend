package com.example.pos_backend.repository;

import com.example.pos_backend.model.Order;
import com.example.pos_backend.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    List<OrderItem> findByOrder(Order order);
    // İleride bir siparişe ait tüm kalemleri bulmak için
    // List<OrderItem> findByOrder(Order order);
}