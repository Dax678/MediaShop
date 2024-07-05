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
@AllArgsConstructor
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "parent_id", nullable = false)
    private Long parent_id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "meta_title", nullable = false)
    private String metaTitle;

    @Column(name = "meta_description", nullable = false)
    private String metaDescription;

    @Column(name = "meta_keywords", nullable = false)
    private String metaKeywords;

    @Column(name = "slug", nullable = false)
    private String slug;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id", referencedColumnName = "id",
            insertable = false, updatable = false)
    @JsonIgnore
    private Category parent;

    @OneToMany(mappedBy = "parent")
    @JsonIgnore
    private List<Category> subcategories;

    @OneToMany(mappedBy = "category")
    @JsonIgnore
    private List<ProductCategory> products;

    public Category(Long parent_id, String title, String description, String metaTitle, String metaDescription, String metaKeywords, String slug, Category parent, List<Category> subcategories, List<ProductCategory> products) {
        this.parent_id = parent_id;
        this.title = title;
        this.description = description;
        this.metaTitle = metaTitle;
        this.metaDescription = metaDescription;
        this.metaKeywords = metaKeywords;
        this.slug = slug;
        this.parent = parent;
        this.subcategories = subcategories;
        this.products = products;
    }
}
