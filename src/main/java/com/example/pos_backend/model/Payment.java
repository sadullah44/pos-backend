package com.example.pos_backend.model; // Paket adınızın bu olduğundan emin olun
import com.fasterxml.jackson.annotation.JsonFormat; // Bu satırı ekleyin
import jakarta.persistence.*;
import jakarta.persistence.Table;
import java.math.BigDecimal; // Bu import'u ekleyin
import java.time.LocalDateTime; // Ödeme zamanı için

@Entity
@Table(name = "payments") // Veritabanı tablo adı
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentID; // Sizin isteğiniz: paymentID

    // Sizin isteğiniz: orderID (Hangi siparişin ödemesi)
    // 'Parçalı ödeme' kuralı (@ManyToOne)
    @ManyToOne
    @JoinColumn(name = "orderID", nullable = false)
    private Order order;

    // Sizin isteğiniz: methodID (Hangi yöntemle ödendi)
    // (Biz bu sınıfı Adım 23'te 'PaymentMethod' olarak oluşturmuştuk)
    @ManyToOne
    @JoinColumn(name = "paymentMethodID", nullable = false) // 'PaymentMethod'un ID'sine bağlan
    private PaymentMethod paymentMethod;

    // Sizin isteğiniz: cashierID (Kasiyerin ID'si - Users tablosundan)
    // --- YENİ EKLENEN İLİŞKİ ---
    @ManyToOne
    @JoinColumn(name = "cashierID", nullable = false)
    private User cashier; // 'User' tablosuna bağlan (Kasiyer de bir 'User'dır)

    // Sizin isteğiniz: amountPaid (Ödenen tutar)
    @Column(nullable = false,precision = 10, scale = 2)
    private BigDecimal amountPaid;

    // Sizin isteğiniz: paymentTime (Ödeme zamanı)
    @Column(updatable = false)
    @JsonFormat(pattern = "dd.MM.yyyy HH:mm:ss")
    private LocalDateTime paymentTime;

    // JPA için boş constructor
    public Payment() {
    }

    // Ödeme alındığı an zamanı otomatik kaydet
    @PrePersist
    protected void onPayment() {
        this.paymentTime = LocalDateTime.now();
    }

    // --- Getter ve Setter Metotları (Yeni 'cashier' alanı dahil) ---

    public Long getPaymentID() {
        return paymentID;
    }

    public void setPaymentID(Long paymentID) {
        this.paymentID = paymentID;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public User getCashier() {
        return cashier;
    }

    public void setCashier(User cashier) {
        this.cashier = cashier;
    }

    public BigDecimal getAmountPaid() {
        return amountPaid;
    }

    public void setAmountPaid(BigDecimal amountPaid) {
        this.amountPaid = amountPaid;
    }

    public LocalDateTime getPaymentTime() {
        return paymentTime;
    }

    public void setPaymentTime(LocalDateTime paymentTime) {
        this.paymentTime = paymentTime;
    }
}