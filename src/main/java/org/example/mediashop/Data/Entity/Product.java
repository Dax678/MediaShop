package org.example.mediashop.Data.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "product", schema = "public")
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
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

    @Column(name = "unit_price", nullable = false)
    private Double unitPrice;

    @Column(name = "quantity_per_unit", nullable = false)
    private Integer quantityPerUnit;

    @OneToMany(mappedBy = "product")
    @JsonIgnore
    private List<ProductCategory> categories;

    @OneToMany(mappedBy = "product")
    @JsonIgnore
    private List<ProductAttribute> attributes;

    @OneToMany(mappedBy = "product")
    @JsonIgnore
    private List<OrderItem> orders;

    public Product(String name, String description, String shortDescription, String brand, String image, Double unitPrice, Integer quantityPerUnit, List<ProductCategory> categories, List<ProductAttribute> attributes, List<OrderItem> orders) {
        this.name = name;
        this.description = description;
        this.shortDescription = shortDescription;
        this.brand = brand;
        this.image = image;
        this.unitPrice = unitPrice;
        this.quantityPerUnit = quantityPerUnit;
        this.categories = categories;
        this.attributes = attributes;
        this.orders = orders;
    }
}
