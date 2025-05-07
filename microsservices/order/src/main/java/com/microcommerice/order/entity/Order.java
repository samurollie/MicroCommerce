package com.microcommerice.order.entity;

import com.microcommerice.order.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "app_orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String customerId;

    @Column(nullable = false, name = "order_date")
    private LocalDateTime orderDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status;

    @Column(name = "items_total_amount", nullable = false, precision = 19, scale = 4)
    private BigDecimal itemsTotalAmount; // Soma dos subtotais dos itens

    @Column(name = "shipping_amount", nullable = false, precision = 19, scale = 4)
    private BigDecimal shippingAmount;

    @Column(name = "discount_amount", precision = 19, scale = 4)
    private BigDecimal discountAmount = BigDecimal.ZERO;

    @Column(name = "grand_total_amount", nullable = false, precision = 19, scale = 4)
    private BigDecimal grandTotalAmount; // itemsTotalAmount + shippingAmount - discountAmount

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<OrderItem> items = new ArrayList<>();

    @Column(name = "shipping_address_id") // Poderia ser um ID para uma entidade de Endereço ou um objeto embutido/JSON
    private String shippingAddressId; // Simplificado: ID da cotação de frete ou endereço

    @Column(name = "payment_transaction_id")
    private String paymentTransactionId; // ID da transação de pagamento

    @Column(name = "shipping_tracking_id")
    private String shippingTrackingId;

    public void addItem(OrderItem item) {
        items.add(item);
        item.setOrder(this);
    }

    public void calculateTotals() {
        this.itemsTotalAmount = items.stream()
                .map(OrderItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        if (this.shippingAmount == null) {
            this.shippingAmount = BigDecimal.ZERO;
        }
        if (this.discountAmount == null) {
            this.discountAmount = BigDecimal.ZERO;
        }
        this.grandTotalAmount = this.itemsTotalAmount.add(this.shippingAmount).subtract(this.discountAmount);
    }

    @PrePersist
    protected void onCreate() {
        this.orderDate = LocalDateTime.now();
        if (this.id == null) {
            this.id = UUID.randomUUID();
        }
        if (this.status == null) {
            this.status = OrderStatus.PENDING_PAYMENT; // Status inicial padrão
        }
        calculateTotals();
    }

    @PreUpdate
    protected void onUpdate() {
        calculateTotals();
    }
}