package com.example.pos_backend.service; // Paket adınızın bu olduğundan emin olun

import com.example.pos_backend.model.Table;
import com.example.pos_backend.repository.TableRepository;
import jakarta.persistence.EntityNotFoundException; // Hata için import
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service // Bu sınıfın bir "Servis" (Beyin) katmanı olduğunu Spring'e bildirir.
public class TableService {

    private final TableRepository tableRepository; // Müteahhit

    // Servis, konuşacağı 'Repository' (müteahhit) olmadan çalışamaz.
    // @Autowired ile Spring'den 'TableRepository'nin bir kopyasını istiyoruz.
    @Autowired
    public TableService(TableRepository tableRepository) {
        this.tableRepository = tableRepository;
    }

    /**
     * Tüm masaları getiren metot.
     * (Bu, /masalar endpoint'imizin yeni beyni olacak)
     */
    public List<Table> getAllTables() {
        return tableRepository.findAll();
    }

    /**
     * SENARYO 2 İÇİN YENİ METOT:
     * Bir masanın durumunu güncelleyen iş mantığı.
     * @Transactional: Bu metot bir veritabanı işlemi yapacak demektir.
     */
    @Transactional
    public Table updateTableStatus(Long tableID, String newStatus) {

        // 1. Masayı ID ile veritabanında bul.
        //    orElseThrow: Eğer masa bulunamazsa, hata fırlat.
        Table tableToUpdate = tableRepository.findById(tableID)
                .orElseThrow(() -> new EntityNotFoundException("Masa bulunamadı, ID: " + tableID));

        // 2. İş Mantığı: Masanın durumunu güncelle.
        tableToUpdate.setStatus(newStatus);

        // 3. Değişiklikleri kaydet.
        // (Not: @Transactional sayesinde 'save' demesek de çalışır ama
        //  açıkça 'save' demek, güncellenmiş nesneyi döndürmeyi garantiler.)
        return tableRepository.save(tableToUpdate);
    }

    // İleride "Masa 1"in detaylarını getirmek için
    // public Table getTableById(Long tableID) {
    //     return tableRepository.findById(tableID)
    //             .orElseThrow(() -> new EntityNotFoundException("Masa bulunamadı, ID: " + tableID));
    // }
}