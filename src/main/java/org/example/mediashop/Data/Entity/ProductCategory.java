package org.example.mediashop.Data.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "product_category", schema = "public")
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class ProductCategory {

    @EmbeddedId
    private ProductCategoryKey id;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @MapsId(value = "productId")
    @JoinColumn(name = "product_id", referencedColumnName = "id",
            insertable = false, updatable = false)
    @JsonIgnore
    private Product product;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @MapsId(value = "categoryId")
    @JoinColumn(name = "category_id", referencedColumnName = "id",
            insertable = false, updatable = false)
    @JsonIgnore
    private Category category;
}
