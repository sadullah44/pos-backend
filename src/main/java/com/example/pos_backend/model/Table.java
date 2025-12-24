package com.example.pos_backend.model;

import jakarta.persistence.*;

@Entity
@jakarta.persistence.Table(name = "tables")
public class Table {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tableID;

    private String tableName;
    private String status;
    private int capacity;

    // --- YENİ EKLENEN ALAN (Müşteri Adı) ---
    private String customerName;

    // Statik sabitler
    public static final String STATUS_AVAILABLE = "BOŞ";
    public static final String STATUS_OCCUPIED = "DOLU";
    public static final String STATUS_RESERVED = "REZERVE";

    public Table() {
    }

    public Table(String tableName, String status, int capacity) {
        this.tableName = tableName;
        this.status = status;
        this.capacity = capacity;
    }

    // --- Getter ve Setter Metotları ---
    public Long getTableID() { return tableID; }
    public void setTableID(Long tableID) { this.tableID = tableID; }

    public String getTableName() { return tableName; }
    public void setTableName(String tableName) { this.tableName = tableName; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public int getCapacity() { return capacity; }
    public void setCapacity(int capacity) { this.capacity = capacity; }

    // --- YENİ GETTER/SETTER (Müşteri Adı İçin) ---
    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }
}