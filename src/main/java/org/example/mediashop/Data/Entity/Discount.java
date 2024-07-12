package org.example.mediashop.Data.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "discount", schema = "public")
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class Discount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "value", nullable = false)
    private Double value;

    @Column(name = "code", nullable = false)
    private String code;

    @Column(name = "start_date", nullable = false)
    private LocalDateTime startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDateTime endDate;

    @Column(name = "max_usage", nullable = false)
    private int maxUsage;

    @Column(name = "min_purchase_amount", nullable = false)
    private Double minPurchaseAmount;

    @Column(name = "created_at", nullable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "discount")
    @JsonIgnore
    private List<ProductDiscount> products;

    public Discount(Double value, String code, LocalDateTime startDate, LocalDateTime endDate, int maxUsage, Double minPurchaseAmount) {
        this.value = value;
        this.code = code;
        this.startDate = startDate;
        this.endDate = endDate;
        this.maxUsage = maxUsage;
        this.minPurchaseAmount = minPurchaseAmount;
    }
}
