package org.example.mediashop.Data.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Embeddable
@RequiredArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class ProductDiscountKey implements Serializable {
    @Column(name = "product_id", nullable = false)
    private Long productId;

    @Column(name = "discount_id", nullable = false)
    private Long discountId;
}
