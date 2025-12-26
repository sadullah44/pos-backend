package com.example.pos_backend.controller;

import com.example.pos_backend.model.Table;
import com.example.pos_backend.service.TableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("")
public class TableController {

    private final TableService tableService;

    @Autowired
    public TableController(TableService tableService) {
        this.tableService = tableService;
    }

    @GetMapping("/masalar")
    public List<Table> getAllTables() {
        return tableService.getAllTables();
    }

    @PutMapping("/masalar/{id}/durum")
    public Table updateTableStatus(
            @PathVariable Long id,
            @RequestParam String newStatus
    ) {
        return tableService.updateTableStatus(id, newStatus);
    }

    @PostMapping("/masalar/ekle")
    public Table addTable(@RequestBody Table table) {
        return tableService.addTable(table);
    }

    @DeleteMapping("/masalar/{id}")
    public void deleteTable(@PathVariable Long id) {
        tableService.deleteTable(id);
    }

    @PutMapping("/masalar/{id}")
    public Table updateTableDetails(@PathVariable Long id, @RequestBody Table table) {
        return tableService.updateTableDetails(id, table);
    }

    // YENİ: Masayı tamamen boşalt (Android PaymentActivity için)
    @PutMapping("/masalar/{tableId}/bosalt")
    public ResponseEntity<Void> clearTable(@PathVariable Long tableId) {
        tableService.updateTableStatus(tableId, "BOŞ");
        return ResponseEntity.ok().build();
    }
}