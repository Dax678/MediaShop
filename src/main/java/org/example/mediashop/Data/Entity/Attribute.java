package org.example.mediashop.Data.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "attribute", schema = "public")
@Getter
@Setter
@RequiredArgsConstructor
public class Attribute {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    // Attribute name example: color, processor
    @Column(name = "name", nullable = false)
    private String name;

    @OneToMany(mappedBy = "attribute")
    @JsonIgnore
    private List<ProductAttribute> products;
}
