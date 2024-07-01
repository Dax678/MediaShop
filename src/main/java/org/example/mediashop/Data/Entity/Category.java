package org.example.mediashop.Data.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "category", schema = "public")
@Getter
@Setter
@RequiredArgsConstructor
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Category parent;

    @OneToMany(mappedBy = "parent")
    @JsonIgnore
    private List<Category> subcategories;

    @Column(name="title", nullable = false)
    private String title;

    @Column(name="description", nullable = false)
    private String description;

    @Column(name="meta_title", nullable = false)
    private String metaTitle;

    @Column(name="meta_description", nullable = false)
    private String metaDescription;

    @Column(name="meta_keywords", nullable = false)
    private String metaKeywords;

    @Column(name="slug", nullable = false)
    private String slug;

    @OneToMany(mappedBy = "category")
    @JsonIgnore
    private List<ProductCategory> products;
}
