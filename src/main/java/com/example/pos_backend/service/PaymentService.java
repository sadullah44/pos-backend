package com.example.pos_backend.service;

import com.example.pos_backend.model.*;
import com.example.pos_backend.repository.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal; // Bu import'u ekleyin
import java.util.List;

@Service
public class PaymentService {

    // ... (Constructor ve Repository alanları aynı, Adım 39'daki gibi 5 repository'li)
    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final PaymentMethodRepository paymentMethodRepository;
    private final TableRepository tableRepository;

    public PaymentService(PaymentRepository paymentRepository,
                          OrderRepository orderRepository,
                          UserRepository userRepository,
                          PaymentMethodRepository paymentMethodRepository,
                          TableRepository tableRepository) {
        this.paymentRepository = paymentRepository;
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.paymentMethodRepository = paymentMethodRepository;
        this.tableRepository = tableRepository;
    }

    /**
     * SENARYO 1 - KISIM 3: "Ödeme Yap" (BigDecimal ile güncellendi)
     * @Transactional: Bu metottaki 3 veritabanı işlemi (Payment yarat, Order güncelle, Table güncelle)
     * bir bütün olarak ele alınır.
     */
    @Transactional
    public Payment processPayment(Long orderID, Long paymentMethodID, Long cashierID,
                                  BigDecimal amountPaid // --- DEĞİŞİKLİK 1: DTO'dan artık 'BigDecimal' geliyor ---
    ) {

        // 1. Gerekli nesneleri ID'leriyle veritabanından bul.
        Order order = orderRepository.findById(orderID)
                .orElseThrow(() -> new EntityNotFoundException("Sipariş bulunamadı, ID: " + orderID));

        PaymentMethod method = paymentMethodRepository.findById(paymentMethodID)
                .orElseThrow(() -> new EntityNotFoundException("Ödeme Yöntemi bulunamadı, ID: " + paymentMethodID));

        User cashier = userRepository.findById(cashierID)
                .orElseThrow(() -> new EntityNotFoundException("Kasiyer (User) bulunamadı, ID: " + cashierID));

        // 2. Yeni 'Payment' (Ödeme) nesnesini oluştur.
        Payment newPayment = new Payment();
        newPayment.setOrder(order);
        newPayment.setPaymentMethod(method);
        newPayment.setCashier(cashier);
        newPayment.setAmountPaid(amountPaid); // 'BigDecimal' tutarı ata
        // 'paymentTime' alanı @PrePersist ile otomatik dolacak

        // 3. Yeni ödemeyi veritabanına kaydet.
        paymentRepository.save(newPayment);

        // --- DEĞİŞİKLİK 2: Toplam Ödeme Hesaplama Mantığı ---

        // 4. O siparişe ait TÜM ödemeleri (parçalı olanlar dahil) bul.
        List<Payment> allPaymentsForThisOrder = paymentRepository.findByOrder(order);

        // 5. Toplam ödenen tutarı hesapla. ('double totalPaidSoFar = 0.0;' yerine)
        BigDecimal totalPaidSoFar = BigDecimal.ZERO;

        for (Payment p : allPaymentsForThisOrder) {
            // 'BigDecimal' toplama metodu
            totalPaidSoFar = totalPaidSoFar.add(p.getAmountPaid());
        }

        // --- DEĞİŞİKLİK 3: Karşılaştırma Mantığı ---
        // 6. İŞ KURALI: Ödeme tamamlandı mı?
        //    Eski 'double' karşılaştırması: (totalPaidSoFar >= order.getTotalAmount() - 0.01)
        //    Yeni 'BigDecimal' karşılaştırması (KESİN):
        //    (totalPaidSoFar, order.getTotalAmount()'dan büyük VEYA ona eşitse)
        if (totalPaidSoFar.compareTo(order.getTotalAmount()) >= 0) {

            // 7. EVET. Sipariş durumunu "ÖDENDİ" yap.
            order.setOrderStatus("ÖDENDİ");
            orderRepository.save(order);

            // 8. İŞ KURALI: Masa artık "Boş".
            Table table = order.getTable();
            if (table != null) {
                table.setStatus(Table.STATUS_AVAILABLE);
                tableRepository.save(table);
            }
        }

        // 9. Oluşturulan yeni 'Payment' nesnesini (ID'si olan) geri döndür.
        return newPayment;
    }
}