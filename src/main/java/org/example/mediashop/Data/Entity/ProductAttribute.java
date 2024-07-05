package org.example.mediashop.Data.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "product_attribute", schema = "public")
@Getter
@Setter
@RequiredArgsConstructor
public class ProductAttribute {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "product_id", nullable = false)
    private Long productId;

    @Column(name = "attribute_id", nullable = false)
    private Long attributeId;

    // Attribute value example: red, Intel
    @Column(name = "value", nullable = false)
    private String value;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", referencedColumnName = "id",
            insertable = false, updatable = false)
    @JsonIgnore
    private Product product;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "attribute_id", referencedColumnName = "id",
            insertable = false, updatable = false)
    @JsonIgnore
    private Attribute attribute;
}
