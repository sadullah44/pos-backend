package com.example.pos_backend.service; // Paket adınızın bu olduğundan emin olun

import com.example.pos_backend.model.Table;
import com.example.pos_backend.model.User;
import com.example.pos_backend.repository.TableRepository;
import com.example.pos_backend.repository.UserRepository;
import org.springframework.boot.CommandLineRunner; // Bu import'a dikkat
import org.springframework.stereotype.Service; // Bu import'a dikkat
import java.util.Arrays;

@Service // 1. Spring'e bunun bir Servis (iş mantığı) sınıfı olduğunu belirtir
public class DatabaseInitializerService implements CommandLineRunner {
    // 2. CommandLineRunner: Spring'e "Uygulama başladığı an 'run' metodumu çalıştır" der.

    private final UserRepository userRepository;
    private final TableRepository tableRepository;

    // 3. @Autowired'ye gerek yok, Spring constructor'dan anlar (Dependency Injection)
    public DatabaseInitializerService(UserRepository userRepository, TableRepository tableRepository) {
        this.userRepository = userRepository;
        this.tableRepository = tableRepository;
    }

    /**
     * Bu metot, sunucu (pos-backend) başladığı AN çalıştırılacak.
     */
    @Override
    public void run(String... args) throws Exception {
        System.out.println("Veritabanı kontrol ediliyor ve başlatılıyor...");
        createDefaultUsers();
        createDefaultTables();
        System.out.println("Veritabanı başlatma işlemi tamamlandı.");
    }

    /**
     * Android projesindeki (MainActivity) sahte kullanıcıları veritabanına kaydeder.
     */
    private void createDefaultUsers() {
        // 4. Eğer veritabanında hiç kullanıcı yoksa (sayısı 0 ise) varsayılanları ekle.
        if (userRepository.count() == 0) {
            System.out.println("Varsayılan kullanıcılar oluşturuluyor...");
            userRepository.save(new User("kasa", "kasa123", User.ROLE_CASHIER));
            userRepository.save(new User("mutfak", "mutfak123", User.ROLE_KITCHEN));
            userRepository.save(new User("garson1", "garson123", User.ROLE_WAITER));
            userRepository.save(new User("garson2", "garson123", User.ROLE_WAITER));
        } else {
            System.out.println("Kullanıcılar zaten mevcut.");
        }
    }

    /**
     * Android projesindeki (WaiterActivity) sahte masaları veritabanına kaydeder.
     */
    private void createDefaultTables() {
        // 5. Eğer veritabanında hiç masa yoksa (sayısı 0 ise) varsayılanları ekle.
        if (tableRepository.count() == 0) {
            System.out.println("Varsayılan masalar oluşturuluyor...");
            // (Android projenizdeki initializeTables metodundan alınmıştır)
            tableRepository.saveAll(Arrays.asList(
                    new Table("Masa 1", Table.STATUS_AVAILABLE, 4),
                    new Table("Masa 2", Table.STATUS_OCCUPIED, 6),
                    new Table("Masa 3", Table.STATUS_RESERVED, 4),
                    new Table("Masa 4", Table.STATUS_AVAILABLE, 8),
                    new Table("Masa 5", Table.STATUS_OCCUPIED, 4),
                    new Table("Masa 6", Table.STATUS_AVAILABLE, 6),
                    new Table("Masa 7", Table.STATUS_AVAILABLE, 2),
                    new Table("Masa 8", Table.STATUS_RESERVED, 4)
            ));
        } else {
            System.out.println("Masalar zaten mevcut.");
        }
    }
}