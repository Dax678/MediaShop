package org.example.mediashop.Repository;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.example.mediashop.Data.Entity.*;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ProductSpecifications {
    public static Specification<Product> withFilters(String categoryName,
                                                     String brandName,
                                                     Double minPrice,
                                                     Double maxPrice,
                                                     Double rating,
                                                     Boolean isAvailable,
                                                     Map<String, String> attributes) {
        return (root, query, builder) -> {
            List<Predicate> predicates = new ArrayList<>();
            Join<Product, ProductCategory> productCategoryJoin = root.join("categories");
            Join<ProductCategory, Category> categoryJoin = productCategoryJoin.join("category");

            if (categoryName != null) {
                predicates.add(builder.equal(categoryJoin.get("title"), categoryName));
            }
            if (brandName != null) {
                predicates.add(builder.equal(root.get("brand"), brandName));
            }
            if (minPrice != null) {
                predicates.add(builder.greaterThanOrEqualTo(root.get("unitPrice"), minPrice));
            }
            if (maxPrice != null) {
                predicates.add(builder.lessThanOrEqualTo(root.get("unitPrice"), maxPrice));
            }
            if (rating != null) {
                predicates.add(builder.equal(root.get("rating"), rating));
            }
            if (isAvailable != null) {
                predicates.add(builder.equal(root.get("isAvailable"), isAvailable));
            }

            // Handle attributes
            if (attributes != null && !attributes.isEmpty()) {
                for (Map.Entry<String, String> entry : attributes.entrySet()) {
                    Join<Product, ProductAttribute> productAttributeJoin = root.join("attributes");
                    Join<ProductAttribute, Attribute> AttributeJoin = productAttributeJoin.join("attribute");

                    predicates.add(builder.and(
                            builder.equal(AttributeJoin.get("name"), entry.getKey()),
                            builder.equal(productAttributeJoin.get("value"), entry.getValue())
                    ));
                    System.out.println("entry.getKey() -> " + entry.getKey() + ", entry.getValue() -> " + entry.getValue());

                }
            }

            return builder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
