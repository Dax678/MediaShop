package org.example.mediashop.Data.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "product_discount", schema = "public")
@Getter
@Setter
@RequiredArgsConstructor
public class ProductDiscount {

    @EmbeddedId
    private ProductDiscountKey id;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @MapsId(value = "productId")
    @JoinColumn(name = "product_id", referencedColumnName = "id",
            insertable = false, updatable = false)
    @JsonIgnore
    private Product product;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @MapsId(value = "discountId")
    @JoinColumn(name = "discount_id", referencedColumnName = "id",
            insertable = false, updatable = false)
    @JsonIgnore
    private Discount discount;
}
