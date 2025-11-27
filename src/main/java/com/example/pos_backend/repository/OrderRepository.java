package com.example.pos_backend.repository;

import com.example.pos_backend.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    // Masa ID'sine göre ve durumu 'ÖDENDİ' OLMAYAN (aktif) siparişi bul
    Optional<Order> findFirstByTable_TableIDAndOrderStatusNot(Long tableId, String status);
}