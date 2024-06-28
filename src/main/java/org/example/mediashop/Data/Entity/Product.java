package org.example.mediashop.Data.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "product", schema = "public")
@Getter
@Setter
@RequiredArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "short_description", nullable = false)
    private String shortDescription;

    @Column(name = "brand", nullable = false)
    private String brand;

    @Column(name = "image", nullable = false)
    private String image;

    @Column(name = "unitPrice", nullable = false)
    private Double unitPrice;

    @Column(name = "quantityPerUnit", nullable = false)
    private Integer quantityPerUnit;

    @OneToMany(mappedBy = "product")
    private List<ProductCategory> categories;
}
