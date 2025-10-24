package com.example.pos_backend.model;

// JPA (veritabanı) için gerekli importlar
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

// Not: Tablo adını 'tables' yapıyoruz çünkü 'table' SQL'de rezerve bir kelime olabilir.
@Entity(name = "tables") 
public class Table {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Otomatik artan ID (1, 2, 3...)
    private Long id;

    private String name; // Örn: "Masa 5"
    private String status; // Örn: "Boş", "Dolu", "Rezerve"
    private int capacity; // Örn: 4 (kişilik)

    // Android projenizdeki gibi sabitler
    public static final String STATUS_AVAILABLE = "Boş";
    public static final String STATUS_OCCUPIED = "Dolu";
    public static final String STATUS_RESERVED = "Rezerve";

    // JPA için BOŞ CONSTRUCTOR şarttır
    public Table() {
    }

    // Veri eklemek için kullanacağımız constructor
    public Table(String name, String status, int capacity) {
        this.name = name;
        this.status = status;
        this.capacity = capacity;
    }

    // --- JPA için GETTER ve SETTER metotları şarttır ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }
}