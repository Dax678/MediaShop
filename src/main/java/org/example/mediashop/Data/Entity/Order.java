package org.example.mediashop.Data.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "order", schema = "public")
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @OneToMany(mappedBy = "order")
    @JsonIgnore
    private List<OrderItem> products;

    @Column(name = "total_amount", nullable = false)
    private Double totalAmount;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @Column(name = "payment_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderPaymentStatus paymentStatus;

    @Column(name = "payment_method", nullable = false)
    private String paymentMethod;

    @Column(name = "shipping_address", nullable = false)
    private String shippingAddress;

    @Column(name = "shipping_method", nullable = false)
    private String shippingMethod;

    @Column(name = "order_date", nullable = false)
    private LocalDate orderDate;

    @Column(name = "delivery_date")
    private LocalDate deliveryDate;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id",
            insertable = false, updatable = false)
    @JsonIgnore
    private User user;

    public Order(Long userId, Double totalAmount, OrderStatus status, OrderPaymentStatus paymentStatus, String paymentMethod, String shippingAddress, String shippingMethod, LocalDate orderDate, LocalDate deliveryDate) {
        this.userId = userId;
        this.totalAmount = totalAmount;
        this.status = status;
        this.paymentStatus = paymentStatus;
        this.paymentMethod = paymentMethod;
        this.shippingAddress = shippingAddress;
        this.shippingMethod = shippingMethod;
        this.orderDate = orderDate;
        this.deliveryDate = deliveryDate;
    }
}
