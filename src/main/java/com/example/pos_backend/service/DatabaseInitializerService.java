package com.example.pos_backend.service;

import com.example.pos_backend.model.*;
import com.example.pos_backend.repository.*;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal; // Bu import'u ekleyin
import java.util.Arrays;
import java.util.List;

@Service
public class DatabaseInitializerService {

    // ... (Tüm 8 Repository'yi enjekte etme kısmı ve Constructor (Yapıcı Metot) aynı)

    private final UserRepository userRepository;
    private final TableRepository tableRepository;
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final PaymentMethodRepository paymentMethodRepository;
    private final PaymentRepository paymentRepository;

    public DatabaseInitializerService(UserRepository userRepository,
                                      TableRepository tableRepository,
                                      CategoryRepository categoryRepository,
                                      ProductRepository productRepository,
                                      OrderRepository orderRepository,
                                      OrderItemRepository orderItemRepository,
                                      PaymentMethodRepository paymentMethodRepository,
                                      PaymentRepository paymentRepository) {
        this.userRepository = userRepository;
        this.tableRepository = tableRepository;
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.paymentMethodRepository = paymentMethodRepository;
        this.paymentRepository = paymentRepository;
    }

    // ... (initDatabase, createDefaultUsers, createDefaultTables, createDefaultCategories metotları aynı) ...
    // Sadece para ile ilgili metotları değiştiriyoruz.
    // Bu yüzden tüm dosyayı kopyalamak en kolayı.

    @PostConstruct
    @Transactional
    public void initDatabase() {
        System.out.println("Veritabanı kontrol ediliyor ve başlatılıyor...");

        createDefaultUsers();
        createDefaultTables();
        createDefaultCategories();
        createDefaultPaymentMethods(); // Bu da parayla ilgili değil, sırası değişebilir.

        // Parayla ilgili olanlar:
        createDefaultProducts();
        createDefaultOrdersAndItems();
        createDefaultPayments();

        System.out.println("Veritabanı başlatma işlemi tamamlandı.");
    }

    private void createDefaultUsers() {
        if (userRepository.count() == 0) {
            System.out.println("Varsayılan kullanıcılar oluşturuluyor...");
            userRepository.saveAll(Arrays.asList(
                    new User("kasa", "kasa123", User.ROLE_CASHIER),
                    new User("garson1", "garson123", User.ROLE_WAITER),
                    new User("mutfak", "mutfak123", User.ROLE_KITCHEN)
            ));
        } else {
            System.out.println("Kullanıcılar zaten mevcut.");
        }
    }

    private void createDefaultTables() {
        if (tableRepository.count() == 0) {
            System.out.println("Varsayılan masalar oluşturuluyor...");
            tableRepository.saveAll(Arrays.asList(
                    new Table("Masa 1", Table.STATUS_AVAILABLE),
                    new Table("Masa 2", Table.STATUS_OCCUPIED),
                    new Table("Masa 3", Table.STATUS_RESERVED),
                    new Table("Masa 4", Table.STATUS_AVAILABLE),
                    new Table("Masa 5", Table.STATUS_AVAILABLE)
            ));
        } else {
            System.out.println("Masalar zaten mevcut.");
        }
    }

    private void createDefaultCategories() {
        if (categoryRepository.count() == 0) {
            System.out.println("Yer tutucu Kategoriler oluşturuluyor...");
            Category corbalar = new Category();
            corbalar.setCategoryName("Çorbalar");
            corbalar.setSortOrder(1);

            Category icecekler = new Category();
            icecekler.setCategoryName("İçecekler");
            icecekler.setSortOrder(10);

            categoryRepository.saveAll(Arrays.asList(corbalar, icecekler));
        } else {
            System.out.println("Kategoriler zaten mevcut.");
        }
    }

    private void createDefaultPaymentMethods() {
        if (paymentMethodRepository.count() == 0) {
            System.out.println("Ödeme Yöntemleri oluşturuluyor...");
            PaymentMethod nakit = new PaymentMethod("Nakit");
            PaymentMethod krediKarti = new PaymentMethod("Kredi Kartı");
            PaymentMethod qrKod = new PaymentMethod("QR Kod");

            paymentMethodRepository.saveAll(Arrays.asList(nakit, krediKarti, qrKod));
        } else {
            System.out.println("Ödeme Yöntemleri zaten mevcut.");
        }
    }

    // --- DEĞİŞİKLİK 1: Ürün Fiyatları ---
    private void createDefaultProducts() {
        if (productRepository.count() == 0) {
            System.out.println("Yer tutucu Ürünler oluşturuluyor...");

            Category corbaKategorisi = categoryRepository.findAll().get(0);
            Category icecekKategorisi = categoryRepository.findAll().get(1);

            Product p1 = new Product();
            p1.setProductName("İşkembe Çorbası");
            // 'double' (120.0) yerine 'BigDecimal' nesnesi
            p1.setBasePrice(new BigDecimal("120.00"));
            p1.setCategory(corbaKategorisi);
            p1.setKitchenItem(true);
            p1.setAvailable(true);

            Product p2 = new Product();
            p2.setProductName("Mercimek Çorbası");
            p2.setBasePrice(new BigDecimal("100.00")); // DEĞİŞİKLİK
            p2.setCategory(corbaKategorisi);
            p2.setKitchenItem(true);
            p2.setAvailable(true);

            Product p3 = new Product();
            p3.setProductName("Kola");
            p3.setBasePrice(new BigDecimal("50.00")); // DEĞİŞİKLİK
            p3.setCategory(icecekKategorisi);
            p3.setKitchenItem(false);
            p3.setAvailable(true);

            productRepository.saveAll(Arrays.asList(p1, p2, p3));
        } else {
            System.out.println("Ürünler zaten mevcut.");
        }
    }

    // --- DEĞİŞİKLİK 2: Sipariş Toplam Tutar Hesaplaması ---
    private void createDefaultOrdersAndItems() {
        if (orderRepository.count() == 0) {
            System.out.println("Örnek Siparişler ve Kalemleri oluşturuluyor...");

            User garson1 = userRepository.findByUsername("garson1").orElse(null);
            Table masa2 = tableRepository.findAll().get(1); // 'Masa 2'
            Product iskembe = productRepository.findAll().get(0);
            Product kola = productRepository.findAll().get(2);

            Order siparis1 = new Order();
            siparis1.setTable(masa2);
            siparis1.setWaiter(garson1);
            siparis1.setOrderStatus("ÖDENDİ");
            siparis1.setTotalAmount(BigDecimal.ZERO); // '0.0' yerine 'BigDecimal.ZERO'
            orderRepository.save(siparis1);

            OrderItem item1 = new OrderItem();
            item1.setOrder(siparis1);
            item1.setProduct(iskembe);
            item1.setQuantity(2);
            item1.setPriceAtOrder(iskembe.getBasePrice()); // 'BigDecimal' atandı
            item1.setItemNotes("Bol sarımsaklı");
            item1.setKitchenStatus("hazır");
            item1.setServed(true);

            OrderItem item2 = new OrderItem();
            item2.setOrder(siparis1);
            item2.setProduct(kola);
            item2.setQuantity(1);
            item2.setPriceAtOrder(kola.getBasePrice()); // 'BigDecimal' atandı
            item2.setItemNotes("Buzsuz");
            item2.setKitchenStatus("hazır");
            item2.setServed(true);

            orderItemRepository.saveAll(Arrays.asList(item1, item2));

            // 'BigDecimal' ile Toplam Tutar Hesaplaması
            // (2 * 120.00)
            BigDecimal toplam1 = item1.getPriceAtOrder().multiply(new BigDecimal(item1.getQuantity()));
            // (1 * 50.00)
            BigDecimal toplam2 = item2.getPriceAtOrder().multiply(new BigDecimal(item2.getQuantity()));
            // (240.00 + 50.00)
            BigDecimal toplam = toplam1.add(toplam2); // Sonuç: 290.00

            siparis1.setTotalAmount(toplam); // 'BigDecimal' toplam atandı
            orderRepository.save(siparis1);
        } else {
            System.out.println("Siparişler zaten mevcut.");
        }
    }

    // --- DEĞİŞİKLİK 3: Ödeme Tutar Ataması ---
    private void createDefaultPayments() {
        if (paymentRepository.count() == 0) {
            System.out.println("Örnek Ödeme Kayıtları oluşturuluyor...");

            Order siparis1 = orderRepository.findAll().get(0);
            User kasa = userRepository.findByUsername("kasa").orElse(null);
            PaymentMethod nakitYontemi = paymentMethodRepository.findAll().get(0);

            Payment odeme1 = new Payment();
            odeme1.setOrder(siparis1);
            odeme1.setPaymentMethod(nakitYontemi);
            odeme1.setCashier(kasa);

            // Siparişin 'BigDecimal' olan toplam tutarını,
            // ödemenin 'BigDecimal' olan 'amountPaid' alanına ata.
            odeme1.setAmountPaid(siparis1.getTotalAmount());

            paymentRepository.save(odeme1);
        } else {
            System.out.println("Ödemeler zaten mevcut.");
        }
    }
}