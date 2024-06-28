package org.example.mediashop.Data.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Embeddable
@RequiredArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class ProductCategoryKey implements Serializable {
    @Column(name = "product_id", nullable = false)
    private Long productId;

    @Column(name = "category_id", nullable = false)
    private Long categoryId;
}
