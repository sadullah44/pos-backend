package com.example.pos_backend.controller; // Paket adınızın bu olduğundan emin olun

import com.example.pos_backend.model.Table;
import com.example.pos_backend.service.TableService; // DEĞİŞİKLİK 1: Artık Repository değil, Service import ediyoruz.
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*; // @PutMapping, @PathVariable, @RequestParam için import

import java.util.List;

@CrossOrigin("*") // Android (veya Postman) tarafının erişebilmesi için
@RestController
@RequestMapping("") // Tüm masa endpoint'lerinin başı '/api'
public class TableController {

    // --- DEĞİŞİKLİK 1 ---
    // Artık Repository'ye (Müteahhit) değil, Service'e (Beyin) bağlanıyoruz.
    private final TableService tableService;

    // @Autowired ile Spring'den 'TableService'i istiyoruz.
    @Autowired
    public TableController(TableService tableService) {
        this.tableService = tableService;
    }

    // --- DEĞİŞİKLİK 2 ---
    // Bu 'GET /masalar' metodu (Adım 8-9'da yaptığımız)
    // artık işin mantığını 'TableService'e devrediyor.
    @GetMapping("/masalar")
    public List<Table> getAllTables() {
        // KAPI (Controller), İŞİ BEYNE (Service) PASLAR
        return tableService.getAllTables();
    }

    // --- YENİ ENDPOINT (SENARYO 2) ---
    // Bu, "Masa durumunu değiştir (boş -> dolu)" senaryonuzu gerçekleştiren API kapısıdır.
    // URL: http://localhost:8080/api/masalar/{id}/durum?yeniDurum=dolu

    /**
     * Bir masanın durumunu günceller.
     * @param id Masa ID'si (URL'den gelir, örn: 5)
     * @param newStatus Yeni durum (Parametreden gelir, örn: "dolu")
     * @return Güncellenmiş Masa nesnesi
     */
    @PutMapping("/masalar/{id}/durum")
    public Table updateTableStatus(
            @PathVariable Long id,                 // @PathVariable: URL'deki {id} kısmını (örn: 5) alır.
            @RequestParam String newStatus         // @RequestParam: URL'deki ?newStatus=... kısmını (örn: "dolu") alır.
    ) {
        // KAPI (Controller), İŞİ BEYNE (Service) PASLAR
        // "Beyne diyoruz ki: 5 numaralı masanın durumunu 'dolu' olarak güncelle."
        return tableService.updateTableStatus(id, newStatus);
    }
    // --- DÜZELTİLMİŞ KISIM ---
    // URL: http://localhost:8080/api/masalar/ekle
    @PostMapping("/masalar/ekle") // Yolu belirginleştirdim
    public Table addTable(@RequestBody Table table) {
        // HATA GİDERİLDİ: Artık repository değil, service kullanılıyor.
        return tableService.addTable(table);
    }
    // --- YENİ: MASA SİLME (Admin İçin) ---
    // URL: DELETE http://localhost:8080/api/masalar/{id}
    @DeleteMapping("/masalar/{id}")
    public void deleteTable(@PathVariable Long id) {
        tableService.deleteTable(id);
    }

    // --- YENİ: MASA BİLGİLERİNİ GÜNCELLEME (Admin İçin) ---
    // URL: PUT http://localhost:8080/api/masalar/{id}
    @PutMapping("/masalar/{id}")
    public Table updateTableDetails(@PathVariable Long id, @RequestBody Table table) {
        return tableService.updateTableDetails(id, table);
    }
}