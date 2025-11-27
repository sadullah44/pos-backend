package com.example.pos_backend.model; // Paket adınızın bu olduğundan emin olun

import jakarta.persistence.*;

@Entity
@jakarta.persistence.Table(name = "tables")
public class Table {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tableID; // Sizin isteğiniz: tableID (id yerine)

    private String tableName; // Sizin isteğiniz: tableName (name yerine)

    private String status; // Sizin isteğiniz: status

    // Eski 'capacity' alanı sizin yeni listenizde olmadığı için kaldırıldı.

    // Statik sabitler (Bunlar kalabilir, kullanışlıdır)
    public static final String STATUS_AVAILABLE = "BOŞ";
    public static final String STATUS_OCCUPIED = "DOLU";
    public static final String STATUS_RESERVED = "REZERVE";

    // JPA için boş constructor
    public Table() {
    }

    // Android'de (Adım 6'daki DatabaseInitializerService)
    // başlangıç verilerini oluşturmak için kullandığımız constructor
    public Table(String tableName, String status) {
        this.tableName = tableName;
        this.status = status;
    }

    // --- Getter ve Setter Metotları ---

    public Long getTableID() {
        return tableID;
    }

    public void setTableID(Long tableID) {
        this.tableID = tableID;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}