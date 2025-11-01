package com.example.pos_backend.service;

import com.example.pos_backend.model.*;
import com.example.pos_backend.repository.*;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class DatabaseInitializerService {

    // --- YENİ EKLENDİ: Artık 9 'Müteahhit' (Repository) var ---
    private final UserRepository userRepository;
    private final RoleRepository roleRepository; // YENİ EKLENDİ
    private final TableRepository tableRepository;
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final PaymentMethodRepository paymentMethodRepository;
    private final PaymentRepository paymentRepository;

    // --- GÜNCELLENDİ: Constructor (Yapıcı Metot) ---
    public DatabaseInitializerService(UserRepository userRepository,
                                      RoleRepository roleRepository, // YENİ EKLENDİ
                                      TableRepository tableRepository,
                                      CategoryRepository categoryRepository,
                                      ProductRepository productRepository,
                                      OrderRepository orderRepository,
                                      OrderItemRepository orderItemRepository,
                                      PaymentMethodRepository paymentMethodRepository,
                                      PaymentRepository paymentRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository; // YENİ EKLENDİ
        this.tableRepository = tableRepository;
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.paymentMethodRepository = paymentMethodRepository;
        this.paymentRepository = paymentRepository;
    }

    @PostConstruct
    @Transactional
    public void initDatabase() {
        System.out.println("Veritabanı kontrol ediliyor ve başlatılıyor (Yeni Mimari)...");

        // --- GÜNCELLENDİ: Veri Ekleme Sırası (Bağımlılık Zinciri) ---

        // 1. Bağımsız Veriler (Bizim + Arkadaşın)
        createDefaultTables(); // Bizim Masalar
        createDefaultPaymentMethods(); // Bizim Ödeme Yöntemleri
        createDefaultRoles(); // Arkadaşın Rolleri

        // 2. 'Roles'e bağımlı
        createDefaultUsers(); // Arkadaşın Kullanıcıları

        // 3. 'Categories' (Bağımsız)
        createDefaultCategories(); // Arkadaşın Kategorileri

        // 4. 'Categories'e bağımlı
        createDefaultProducts(); // Arkadaşın Ürünleri

        // 5. 'Users', 'Tables', 'Products'a bağımlı (GÜNCELLENMİŞ Test Verisi)
        createDefaultOrdersAndItems(); // Bizim Test Siparişimiz (Yeni Verilerle)

        // 6. 'Order', 'User', 'PaymentMethod'a bağımlı (GÜNCELLENMİŞ Test Verisi)
        createDefaultPayments(); // Bizim Test Ödememiz (Yeni Verilerle)

        System.out.println("Veritabanı başlatma işlemi tamamlandı.");
    }

    // --- YENİ METOT: Arkadaşınızın 'INSERT INTO Roles' SQL'inin Java çevirisi ---
    private void createDefaultRoles() {
        if (roleRepository.count() == 0) {
            System.out.println("Varsayılan Roller oluşturuluyor...");
            roleRepository.saveAll(Arrays.asList(
                    new Role("Admin"),   // Arkadaşınızın verisi
                    new Role("Garson"),  // Arkadaşınızın verisi
                    new Role("Kasiyer")  // Arkadaşınızın verisi
            ));
        } else {
            System.out.println("Roller zaten mevcut.");
        }
    }

    // --- GÜNCELLENMİŞ METOT: Arkadaşınızın 'INSERT INTO Users' SQL'inin Java çevirisi ---
    private void createDefaultUsers() {
        if (userRepository.count() == 0) {
            System.out.println("Varsayılan Kullanıcılar oluşturuluyor...");

            // Yeni 'User' sınıfımız 'Role' nesnesi bekliyor.
            // Önce rolleri veritabanından çekmeliyiz:
            Role adminRole = roleRepository.findByRoleName("Admin").orElse(null);
            Role garsonRole = roleRepository.findByRoleName("Garson").orElse(null);
            Role kasiyerRole = roleRepository.findByRoleName("Kasiyer").orElse(null);

            if (adminRole == null || garsonRole == null || kasiyerRole == null) {
                System.out.println("HATA: Roller bulunamadı. Kullanıcılar oluşturulamadı.");
                return;
            }

            // Arkadaşınızın 'INSERT' SQL'indeki verilerle yeni 'User'lar oluşturma
            // Yeni Constructor: (username, password, fullName, role, isActive)
            userRepository.saveAll(Arrays.asList(
                    new User("adminuser", "hash_admin123", "Ayşe Yılmaz", adminRole, true),
                    new User("garson01", "hash_garson123", "Mehmet Demir", garsonRole, true),
                    new User("kasiyer02", "hash_kasiyer123", "Zeynep Kaya", kasiyerRole, true),
                    new User("deneme_pasif", "hash_deneme", "Pasif Kullanıcı", garsonRole, false)
            ));
        } else {
            System.out.println("Kullanıcılar zaten mevcut.");
        }
    }

    // --- GÜNCELLENMİŞ METOT: Arkadaşınızın 'INSERT INTO Categories' SQL'inin Java çevirisi ---
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

    // --- GÜNCELLENMİŞ METOT: Arkadaşınızın 'INSERT INTO Products' SQL'inin Java çevirisi ---
    private void createDefaultProducts() {
        if (productRepository.count() == 0) {
            System.out.println("Varsayılan Ürünler oluşturuluyor...");

            // Kategorileri isimleriyle bulmamız gerekecek
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

    // --- DEĞİŞMEYEN METOT (Bizim Test Verimiz) ---
    private void createDefaultTables() {
        if (tableRepository.count() == 0) {
            System.out.println("Varsayılan masalar oluşturuluyor...");
            tableRepository.saveAll(Arrays.asList(
                    new Table("Masa 1", Table.STATUS_AVAILABLE),
                    new Table("Masa 2", Table.STATUS_AVAILABLE), // Başlangıçta 'Boş' olsun
                    new Table("Masa 3", Table.STATUS_RESERVED),
                    new Table("Masa 4", Table.STATUS_AVAILABLE),
                    new Table("Masa 5", Table.STATUS_AVAILABLE)
            ));
        } else {
            System.out.println("Masalar zaten mevcut.");
        }
    }

    // --- DEĞİŞMEYEN METOT (Bizim Test Verimiz) ---
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

    // --- GÜNCELLENMİŞ METOT: Bizim Test Siparişimiz (Arkadaşınızın verileriyle) ---
    private void createDefaultOrdersAndItems() {
        if (orderRepository.count() == 0) {
            System.out.println("Örnek Test Siparişi oluşturuluyor...");

            // Arkadaşınızın yeni verilerini çek
            // ...
            User garson = userRepository.findByUsername("garson01").orElse(null);
            Table masa2 = tableRepository.findByTableName("Masa 2").orElse(null);
            Product kofte = productRepository.findByProductName("Izgara Köfte").orElse(null);
            Product kola = productRepository.findByProductName("Kola (Kutu)").orElse(null);
// ...

            if (garson == null || masa2 == null || kofte == null || kola == null) {
                System.out.println("HATA: Test Siparişi oluşturmak için gerekli (Kullanıcı, Masa veya Ürün) verileri bulunamadı.");
                return;
            }

            Order siparis1 = new Order();
            siparis1.setTable(masa2);
            siparis1.setWaiter(garson);
            siparis1.setOrderStatus("ÖDENDİ"); // Test ödemesi ekleyeceğiz
            siparis1.setTotalAmount(BigDecimal.ZERO);
            orderRepository.save(siparis1);

            OrderItem item1 = new OrderItem();
            item1.setOrder(siparis1);
            item1.setProduct(kofte); // Eski: İşkembe
            item1.setQuantity(2); // 2 x 120.00 = 240.00
            item1.setPriceAtOrder(kofte.getBasePrice());
            item1.setItemNotes("İyi pişmiş");
            item1.setKitchenStatus("hazır");
            item1.setServed(true);

            OrderItem item2 = new OrderItem();
            item2.setOrder(siparis1);
            item2.setProduct(kola); // Eski: Kola
            item2.setQuantity(1); // 1 x 25.00 = 25.00
            item2.setPriceAtOrder(kola.getBasePrice());
            item2.setItemNotes("");
            item2.setKitchenStatus("hazır");
            item2.setServed(true);

            orderItemRepository.saveAll(Arrays.asList(item1, item2));

            // Toplam Tutar (BigDecimal): (2 * 120.00) + (1 * 25.00) = 265.00
            BigDecimal toplam = kofte.getBasePrice().multiply(new BigDecimal(item1.getQuantity()))
                    .add(kola.getBasePrice().multiply(new BigDecimal(item2.getQuantity())));

            siparis1.setTotalAmount(toplam);
            orderRepository.save(siparis1);

            // Masayı da 'Dolu' yapalım, çünkü sipariş 'ÖDENDİ' olsa da verisi orada
            masa2.setStatus(Table.STATUS_OCCUPIED);
            tableRepository.save(masa2);
        } else {
            System.out.println("Siparişler zaten mevcut.");
        }
    }

    // --- GÜNCELLENMİŞ METOT: Bizim Test Ödememiz (Arkadaşınızın verileriyle) ---
    private void createDefaultPayments() {
        if (paymentRepository.count() == 0) {
            System.out.println("Örnek Test Ödemesi oluşturuluyor...");

            Order siparis1 = orderRepository.findAll().get(0);
            User kasiyer = userRepository.findByUsername("kasiyer02").orElse(null); // Eski: 'kasa'
            PaymentMethod nakitYontemi = paymentMethodRepository.findAll().get(0);

            if (siparis1 == null || kasiyer == null || nakitYontemi == null) {
                System.out.println("HATA: Test Ödemesi oluşturmak için gerekli (Sipariş, Kasiyer veya Yöntem) verileri bulunamadı.");
                return;
            }

            Payment odeme1 = new Payment();
            odeme1.setOrder(siparis1);
            odeme1.setPaymentMethod(nakitYontemi);
            odeme1.setCashier(kasiyer); // Yeni 'kasiyer02'
            odeme1.setAmountPaid(siparis1.getTotalAmount()); // 265.00

            paymentRepository.save(odeme1);
        } else {
            System.out.println("Ödemeler zaten mevcut.");
        }
    }
}