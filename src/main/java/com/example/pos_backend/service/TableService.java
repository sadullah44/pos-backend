package com.example.pos_backend.service;

import com.example.pos_backend.model.Order;
import com.example.pos_backend.model.Table;
import com.example.pos_backend.repository.OrderRepository; // EKLENDİ
import com.example.pos_backend.repository.TableRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class TableService {

    private final TableRepository tableRepository;
    private final OrderRepository orderRepository; // EKLENDİ: Siparişleri kontrol etmek için

    @Autowired
    public TableService(TableRepository tableRepository, OrderRepository orderRepository) {
        this.tableRepository = tableRepository;
        this.orderRepository = orderRepository;
    }

    public List<Table> getAllTables() {
        List<Table> tables = tableRepository.findAll();

        // --- OTOMATİK DÜZELTME MEKANİZMASI ---
        for (Table table : tables) {
            boolean isReallyOccupied = checkIfTableHasActiveOrder(table.getTableID());

            // Gerçek durum ne olmalı?
            String realStatus = isReallyOccupied ? "DOLU" : "BOŞ";

            // Eğer veritabanındaki durum yanlışsa, düzelt ve kaydet
            if (!realStatus.equalsIgnoreCase(table.getStatus())) {
                table.setStatus(realStatus);
                tableRepository.save(table);
            }
        }
        return tables;
    }

    // Masada aktif (kapanmamış) sipariş var mı?
    private boolean checkIfTableHasActiveOrder(Long tableId) {
        // OrderRepository'de findByTable_TableID metodunun olduğundan emin ol
        List<Order> orders = orderRepository.findByTable_TableID(tableId);

        for (Order order : orders) {
            String s = order.getOrderStatus();
            // Bu durumlardan biri varsa masa doludur
            if ("YENI".equals(s) || "BEKLIYOR".equals(s) || "HAZIRLANIYOR".equals(s) ||
                    "HAZIR".equals(s) || "ODEME_BEKLIYOR".equals(s) || "BEKLEMEDE".equals(s)) {
                return true;
            }
        }
        return false;
    }

    @Transactional
    public Table updateTableStatus(Long tableID, String newStatus) {
        Table tableToUpdate = tableRepository.findById(tableID)
                .orElseThrow(() -> new EntityNotFoundException("Masa bulunamadı, ID: " + tableID));

        tableToUpdate.setStatus(newStatus);
        return tableRepository.save(tableToUpdate);
    }
}