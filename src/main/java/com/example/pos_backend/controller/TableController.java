package com.example.pos_backend.controller; // Paket adınızın bu olduğundan emin olun

import com.example.pos_backend.model.Table;
import com.example.pos_backend.repository.TableRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController // 1. Spring'e bu sınıfın bir API Kontrolcüsü olduğunu belirtir.

public class TableController {

    private final TableRepository tableRepository;

    // 3. TableRepository'yi buraya enjekte ediyoruz (Dependency Injection)
    public TableController(TableRepository tableRepository) {
        this.tableRepository = tableRepository;
    }

    /**
     * TÜM MASALARI GETİREN API ENDPOINT'İ
     * Bu metoda http://localhost:8080/api/tables adresinden erişilecek.
     */
    @GetMapping("/masalar") // 4. Bu metodu "/tables" adresine (GET isteği) bağlar.
    public List<Table> getAllTables() {
        // 5. Veritabanındaki tüm masaları bul ve döndür.
        // Spring Boot, bu 'List<Table>' listesini OTOMATİK olarak JSON formatına çevirecektir.
        return tableRepository.findAll();
    }
}