package org.example.mediashop.Repository;

import jakarta.transaction.Transactional;
import org.example.mediashop.Data.Entity.ProductDiscount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface ProductDiscountRepository extends JpaRepository<ProductDiscount, Long> {
    @Modifying
    @Transactional
    @Query(value = "TRUNCATE TABLE product_discount, product, discount CASCADE", nativeQuery = true)
    void truncateAllData();
}
