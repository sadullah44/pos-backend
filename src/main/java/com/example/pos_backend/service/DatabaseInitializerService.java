package com.example.pos_backend.service;

import com.example.pos_backend.model.*;
import com.example.pos_backend.repository.*;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Arrays;

@Service
public class DatabaseInitializerService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final TableRepository tableRepository;
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final PaymentMethodRepository paymentMethodRepository;

    public DatabaseInitializerService(UserRepository userRepository,
                                      RoleRepository roleRepository,
                                      TableRepository tableRepository,
                                      CategoryRepository categoryRepository,
                                      ProductRepository productRepository,
                                      PaymentMethodRepository paymentMethodRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.tableRepository = tableRepository;
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
        this.paymentMethodRepository = paymentMethodRepository;
    }

    @PostConstruct
    @Transactional
    public void initDatabase() {
        System.out.println("Veritabanı kontrol ediliyor ve başlatılıyor...");

        // ❌ ARTIK SİPARİŞ / ÖDEME TEMİZLİĞİ YOK
        // clearOrdersAndPayments();

        createDefaultRoles();
        createDefaultUsers();
        createDefaultCategories();
        createDefaultProducts();
        createDefaultPaymentMethods();

        // ✅ Masaları sadece YOKSA oluştur (reset yok)
        createTablesIfNotExist();
    }

    // --- Roller ---
    private void createDefaultRoles() {
        if (roleRepository.count() == 0) {
            System.out.println("Varsayılan Roller oluşturuluyor...");
            roleRepository.saveAll(Arrays.asList(
                    new Role("Admin"),
                    new Role("Garson"),
                    new Role("Kasiyer"),
                    new Role("Mutfak")
            ));
        } else {
            System.out.println("Roller zaten mevcut.");
        }
    }

    // --- Kullanıcılar ---
    private void createDefaultUsers() {
        if (userRepository.count() == 0) {
            System.out.println("Varsayılan Kullanıcılar oluşturuluyor...");

            Role adminRole = roleRepository.findByRoleName("Admin").orElse(null);
            Role garsonRole = roleRepository.findByRoleName("Garson").orElse(null);
            Role kasiyerRole = roleRepository.findByRoleName("Kasiyer").orElse(null);
            Role mutfakRole = roleRepository.findByRoleName("Mutfak").orElse(null);

            if (adminRole == null || garsonRole == null || kasiyerRole == null) {
                System.out.println("HATA: Roller bulunamadı. Kullanıcılar oluşturulamadı.");
                return;
            }

            userRepository.saveAll(Arrays.asList(
                    new User("adminuser", "hash_admin123", "Ayşe Yılmaz", adminRole, true),
                    new User("garson01", "hash_garson123", "Mehmet Demir", garsonRole, true),
                    new User("kasiyer02", "hash_kasiyer123", "Zeynep Kaya", kasiyerRole, true),
                    new User("deneme_pasif", "hash_deneme", "Pasif Kullanıcı", garsonRole, false),
                    new User("mutfak1", "mutfak123", "sado", mutfakRole, true)
            ));
        } else {
            System.out.println("Kullanıcılar zaten mevcut.");
        }
    }

    // --- Kategoriler ---
    private void createDefaultCategories() {
        if (categoryRepository.count() == 0) {
            System.out.println("Varsayılan Kategoriler oluşturuluyor...");

            Category icecekler = new Category();
            icecekler.setCategoryName("İçecekler");
            icecekler.setSortOrder(10);

            Category anaYemekler = new Category();
            anaYemekler.setCategoryName("Ana Yemekler");
            anaYemekler.setSortOrder(20);

            Category tatlilar = new Category();
            tatlilar.setCategoryName("Tatlılar");
            tatlilar.setSortOrder(30);

            Category salatalar = new Category();
            salatalar.setCategoryName("Salatalar");
            salatalar.setSortOrder(40);

            categoryRepository.saveAll(Arrays.asList(icecekler, anaYemekler, tatlilar, salatalar));
        } else {
            System.out.println("Kategoriler zaten mevcut.");
        }
    }

    // --- Ürünler ---
    private void createDefaultProducts() {
        if (productRepository.count() == 0) {
            System.out.println("Varsayılan Ürünler oluşturuluyor...");

            Category icecekler = categoryRepository.findByCategoryName("İçecekler").orElse(null);
            Category anaYemekler = categoryRepository.findByCategoryName("Ana Yemekler").orElse(null);
            Category tatlilar = categoryRepository.findByCategoryName("Tatlılar").orElse(null);
            Category salatalar = categoryRepository.findByCategoryName("Salatalar").orElse(null);

            Product p1 = new Product();
            p1.setProductName("Kola (Kutu)");
            p1.setBasePrice(new BigDecimal("25.00"));
            p1.setCategory(icecekler);
            p1.setKitchenItem(false);
            p1.setAvailable(true);

            Product p2 = new Product();
            p2.setProductName("Filtre Kahve");
            p2.setBasePrice(new BigDecimal("35.50"));
            p2.setCategory(icecekler);
            p2.setKitchenItem(false);
            p2.setAvailable(true);

            Product p3 = new Product();
            p3.setProductName("Izgara Köfte");
            p3.setBasePrice(new BigDecimal("120.00"));
            p3.setCategory(anaYemekler);
            p3.setKitchenItem(true);
            p3.setAvailable(true);

            Product p4 = new Product();
            p4.setProductName("Tavuk Sote");
            p4.setBasePrice(new BigDecimal("95.00"));
            p4.setCategory(anaYemekler);
            p4.setKitchenItem(true);
            p4.setAvailable(true);

            Product p5 = new Product();
            p5.setProductName("Sütlaç");
            p5.setBasePrice(new BigDecimal("45.00"));
            p5.setCategory(tatlilar);
            p5.setKitchenItem(true);
            p5.setAvailable(true);

            Product p6 = new Product();
            p6.setProductName("Kazandibi");
            p6.setBasePrice(new BigDecimal("40.00"));
            p6.setCategory(tatlilar);
            p6.setKitchenItem(true);
            p6.setAvailable(true);

            Product p7 = new Product();
            p7.setProductName("Mevsim Salata");
            p7.setBasePrice(new BigDecimal("55.00"));
            p7.setCategory(salatalar);
            p7.setKitchenItem(true);
            p7.setAvailable(true);

            productRepository.saveAll(Arrays.asList(p1, p2, p3, p4, p5, p6, p7));
        } else {
            System.out.println("Ürünler zaten mevcut.");
        }
    }

    // --- Ödeme Yöntemleri ---
    private void createDefaultPaymentMethods() {
        if (paymentMethodRepository.count() == 0) {
            System.out.println("Ödeme Yöntemleri oluşturuluyor...");
            paymentMethodRepository.saveAll(Arrays.asList(
                    new PaymentMethod("Nakit"),
                    new PaymentMethod("Kredi Kartı"),
                    new PaymentMethod("QR Kod")
            ));
        } else {
            System.out.println("Ödeme Yöntemleri zaten mevcut.");
        }
    }

    // --- Masalar: sadece YOKSA oluştur ---
    private void createTablesIfNotExist() {
        if (tableRepository.count() == 0) {
            System.out.println("Varsayılan masalar oluşturuluyor...");
            tableRepository.saveAll(Arrays.asList(
                    new Table("Masa 1", Table.STATUS_AVAILABLE),
                    new Table("Masa 2", Table.STATUS_AVAILABLE),
                    new Table("Masa 3", Table.STATUS_RESERVED),
                    new Table("Masa 4", Table.STATUS_AVAILABLE),
                    new Table("Masa 5", Table.STATUS_AVAILABLE)
            ));
        } else {
            System.out.println("Masalar zaten mevcut. Reset YAPILMIYOR.");
        }
    }
}
