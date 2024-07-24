package org.example.mediashop.Repository;


import org.example.mediashop.Data.Entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {
    Optional<Product> findProductById(Long id);

    Optional<Product> findProductByName(String title);

    @Query(value = "SELECT pr FROM Product pr " +
            "JOIN ProductDiscount pd ON pr.id = pd.product.id " +
            "JOIN Discount ds ON ds.id = pd.discount.id " +
            "WHERE ds.code =:discountCode " +
            "AND ds.startDate < CURRENT_TIMESTAMP " +
            "AND ds.endDate > CURRENT_TIMESTAMP ")
    List<Product> findProductByDiscountCode(String discountCode);
}

