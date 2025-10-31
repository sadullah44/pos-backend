package com.example.pos_backend.model; // Paket adınızın bu olduğundan emin olun

import jakarta.persistence.*;
import jakarta.persistence.Table;

@Entity
@Table(name = "categories") // Arkadaşınızın 'Categories' tablosu
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id") // Arkadaşınızın alanı: categoryID
    private Long categoryID;

    @Column(name = "category_name", nullable = false) // Arkadaşınızın alanı: categoryName
    private String categoryName;

    @Column(name = "sort_order") // Arkadaşınızın alanı: sortOrder
    private int sortOrder;

    // JPA için boş constructor
    public Category() {
    }

    // --- Getter ve Setter Metotları ---
    // (JPA'nın ve diğer sınıfların (Product) bu alana erişebilmesi için gereklidir)

    public Long getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(Long categoryID) {
        this.categoryID = categoryID;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public int getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(int sortOrder) {
        this.sortOrder = sortOrder;
    }
}