package com.example.pos_backend.service;

import com.example.pos_backend.model.Order;
import com.example.pos_backend.model.Table;
import com.example.pos_backend.repository.OrderRepository;
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
    private final OrderRepository orderRepository;

    @Autowired
    public TableService(TableRepository tableRepository, OrderRepository orderRepository) {
        this.tableRepository = tableRepository;
        this.orderRepository = orderRepository;
    }

    public List<Table> getAllTables() {
        List<Table> tables = tableRepository.findAll();

        for (Table table : tables) {
            // DÜZELTME: Eğer masa REZERVE ise, otomatik kontrolü atla.
            if ("REZERVE".equalsIgnoreCase(table.getStatus())) {
                continue;
            }

            boolean isReallyOccupied = checkIfTableHasActiveOrder(table.getTableID());
            String realStatus = isReallyOccupied ? "DOLU" : "BOŞ";

            if (!realStatus.equalsIgnoreCase(table.getStatus())) {
                table.setStatus(realStatus);
                // Eğer masa BOŞ'a dönüyorsa müşteri ismini de temizleyelim
                if (realStatus.equals("BOŞ")) {
                    table.setCustomerName(null);
                }
                tableRepository.save(table);
            }
        }
        return tables;
    }

    private boolean checkIfTableHasActiveOrder(Long tableId) {
        List<Order> orders = orderRepository.findByTable_TableID(tableId);
        for (Order order : orders) {
            String s = order.getOrderStatus();
            // GÜNCELLEME: Hem boşluklu hem alt çizgili versiyonları kabul et
            if ("YENI".equals(s) || "BEKLIYOR".equals(s) || "HAZIRLANIYOR".equals(s) ||
                    "HAZIR".equals(s) || "BEKLEMEDE".equals(s) ||
                    "ÖDEME BEKLİYOR".equals(s) || "ODEME_BEKLIYOR".equals(s)) {
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

        // Eğer masa BOŞ yapılıyorsa, müşteri ismini sil
        if ("BOŞ".equalsIgnoreCase(newStatus)) {
            tableToUpdate.setCustomerName(null);
        }

        return tableRepository.save(tableToUpdate);
    }

    public Table addTable(Table table) {
        if (table.getStatus() == null || table.getStatus().isEmpty()) {
            table.setStatus(Table.STATUS_AVAILABLE);
        }
        return tableRepository.save(table);
    }

    public void deleteTable(Long tableId) {
        if (!tableRepository.existsById(tableId)) {
            throw new EntityNotFoundException("Silinecek masa bulunamadı ID: " + tableId);
        }
        tableRepository.deleteById(tableId);
    }

    public Table updateTableDetails(Long tableId, Table updatedTable) {
        Table existingTable = tableRepository.findById(tableId)
                .orElseThrow(() -> new EntityNotFoundException("Masa bulunamadı ID: " + tableId));

        if (updatedTable.getTableName() != null) {
            existingTable.setTableName(updatedTable.getTableName());
        }

        if (updatedTable.getCapacity() != 0) {
            existingTable.setCapacity(updatedTable.getCapacity());
        }

        existingTable.setCustomerName(updatedTable.getCustomerName());

        if (updatedTable.getStatus() != null) {
            existingTable.setStatus(updatedTable.getStatus());
        }

        return tableRepository.save(existingTable);
    }
}