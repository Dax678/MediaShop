package org.example.mediashop.Repository;

import jakarta.transaction.Transactional;
import org.example.mediashop.Data.Entity.ProductCategory;
import org.example.mediashop.Data.Entity.ProductCategoryKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductCategoryRepository extends JpaRepository<ProductCategory, ProductCategoryKey> {
    @Modifying
    @Transactional
    @Query(value = "TRUNCATE TABLE product_category, product, category CASCADE", nativeQuery = true)
    void truncateAllData();
}