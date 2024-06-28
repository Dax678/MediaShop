package org.example.mediashop.Data.Entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "product_category", schema = "public")
@Getter
@Setter
@RequiredArgsConstructor
public class ProductCategory {

    @EmbeddedId
    private ProductCategoryKey id;

    @ManyToOne
    @MapsId(value = "productId")
    @JoinColumn(name = "product_id")
    Product product;


    @ManyToOne
    @MapsId(value = "categoryId")
    @JoinColumn(name = "category_id")
    Category category;
}
