package com.example.pos_backend.controller; // Paket adınızın bu olduğundan emin olun

import com.example.pos_backend.dto.CreatePaymentRequest; // Az önce oluşturduğunuz DTO'yu import et
import com.example.pos_backend.model.Payment;
import com.example.pos_backend.service.PaymentService; // 'Ödeme Beyni'ni import et
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*") // Android (veya Postman) tarafının erişebilmesi için
@RestController
@RequestMapping("/odemeler") // Tüm ödeme endpoint'lerinin başı '/api/odemeler'
public class PaymentController {

    // --- 'Beyin' katmanını (Service) enjekte etme ---

    private final PaymentService paymentService;

    // Tek constructor (Yapıcı Metot), @Autowired'e gerek yok
    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    // --- YENİ ENDPOINT (SENARYO 1 - KISIM 3) ---
    // Bu, "Ödeme Yap" senaryonuzu gerçekleştiren API kapısıdır.
    // URL: POST http://localhost:8080/api/odemeler
    // BODY: { "orderID": 1, "paymentMethodID": 1, "cashierID": 1, "amountPaid": 390.0 }

    /**
     * Yeni bir ödeme kaydı alır (Nakit, Kart vb.).
     * @param request JSON gövdesinden gelen 'orderID', 'paymentMethodID', 'cashierID' ve 'amountPaid'.
     * @return Yeni oluşturulan ve ID'si olan 'Payment' nesnesi.
     */
    @PostMapping
    public Payment processNewPayment(
            @RequestBody CreatePaymentRequest request // @RequestBody: Gelen JSON'u bu DTO'ya doldur
    ) {
        // KAPI (Controller), İŞİ BEYNE (Service) PASLAR
        // "Beyne diyoruz ki: Bu bilgilere göre ödemeyi işle."
        return paymentService.processPayment(
                request.getOrderID(),
                request.getPaymentMethodID(),
                request.getCashierID(),
                request.getAmountPaid()
        );
    }
}