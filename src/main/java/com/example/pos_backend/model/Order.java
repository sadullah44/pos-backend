package com.example.pos_backend.model; // Düzeltilmiş paket adınız
import com.fasterxml.jackson.annotation.JsonFormat; // Bu satırı ekleyin
import jakarta.persistence.*; // JPA importları
import java.time.LocalDateTime; // Sipariş zamanı için
import java.math.BigDecimal; // Bu import'u ekleyin

@Entity
@jakarta.persistence.Table(name = "orders") // SQL'de 'order' rezerve bir kelime olduğu için tablo adını 'orders' yapıyoruz.
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id") // Sizin isteğiniz: orderID (sipariş numarası)
    private Long orderID;

    // Sizin isteğiniz: tableID (hangi masa)
    @ManyToOne
    @JoinColumn(name = "table_id", nullable = false)
    private Table table; // Hangi masanın siparişi olduğunu tutar.

    // Sizin isteğiniz: waiterID (hangi garson - Users tablosundan)
    @ManyToOne
    @JoinColumn(name = "waiter_id", nullable = false)
    private User waiter; // Siparişi hangi garsonun (User) aldığını tutar.

    @Column(name = "order_time", updatable = false) // Sizin isteğiniz: orderTime (sipariş zamanı)
    @JsonFormat(pattern = "dd.MM.yyyy HH:mm:ss")
    private LocalDateTime orderTime;

    @Column(name = "order_status") // Sizin isteğiniz: orderStatus (durumu)
    private String orderStatus; // "beklemede", "hazırlanıyor", "tamamlandı"

    @Column(name = "total_amount",precision = 10, scale = 2) // Sizin isteğiniz: totalAmount (toplam tutar)

    private BigDecimal totalAmount;

    // JPA için boş constructor
    public Order() {
    }

    // Bu metot, bir sipariş oluşturulduğu an otomatik olarak zamanı kaydeder.
    @PrePersist
    protected void onCreate() {
        this.orderTime = LocalDateTime.now();
        // Varsayılan durum "beklemede" olabilir
        if (this.orderStatus == null) {
            this.orderStatus = "beklemede";
        }
    }

    // --- Getter ve Setter Metotları (JPA ve Kod için gerekli) ---

    public Long getOrderID() {
        return orderID;
    }

    public void setOrderID(Long orderID) {
        this.orderID = orderID;
    }

    public Table getTable() {
        return table;
    }

    public void setTable(Table table) {
        this.table = table;
    }

    public User getWaiter() {
        return waiter;
    }

    public void setWaiter(User waiter) {
        this.waiter = waiter;
    }

    public LocalDateTime getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(LocalDateTime orderTime) {
        this.orderTime = orderTime;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }
}