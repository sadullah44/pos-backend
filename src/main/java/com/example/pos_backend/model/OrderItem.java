package com.example.pos_backend.model; // Paket adınızın bu olduğundan emin olun
import java.math.BigDecimal; // Bu import'u ekleyin
import jakarta.persistence.*;
import jakarta.persistence.Table;

@Entity
@Table(name = "orderItems")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderItemID; // Sütun adı da 'orderItemID' olacak

    // --- İlişkiler ---
    // İlişkilerde, @JoinColumn'daki 'name' alanı
    // veritabanındaki sütun adını belirler.
    // Bunları da camelCase yapalım: 'orderID' ve 'productID'

    @ManyToOne
    @JoinColumn(name = "orderID", nullable = false) // 'order_id' yerine 'orderID'
    private Order order;

    @ManyToOne
    @JoinColumn(name = "productID", nullable = false) // 'product_id' yerine 'productID'
    private Product product;

    // --- Diğer Alanlar ---
    // @Column etiketleri kaldırıldı.
    // Java adı = Veritabanı Sütun Adı

    private int quantity; // Sütun adı: 'quantity'
    @Column(precision = 10, scale = 2)
    private BigDecimal priceAtOrder; // Sütun adı: 'priceAtOrder'

    private String itemNotes; // Sütun adı: 'itemNotes'

    private String kitchenStatus; // Sütun adı: 'kitchenStatus'

    private boolean isServed; // Sütun adı: 'isServed'

    // JPA için boş constructor
    public OrderItem() {
    }

    // --- Getter ve Setter Metotları ---

    public Long getOrderItemID() {
        return orderItemID;
    }

    public void setOrderItemID(Long orderItemID) {
        this.orderItemID = orderItemID;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPriceAtOrder() {
        return priceAtOrder;
    }

    public void setPriceAtOrder(BigDecimal priceAtOrder) {
        this.priceAtOrder = priceAtOrder;
    }

    public String getItemNotes() {
        return itemNotes;
    }

    public void setItemNotes(String itemNotes) {
        this.itemNotes = itemNotes;
    }

    public String getKitchenStatus() {
        return kitchenStatus;
    }

    public void setKitchenStatus(String kitchenStatus) {
        this.kitchenStatus = kitchenStatus;
    }

    public boolean isServed() {
        return isServed;
    }

    public void setServed(boolean served) {
        isServed = served;
    }
}