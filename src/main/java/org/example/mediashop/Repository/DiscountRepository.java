package org.example.mediashop.Repository;

import org.example.mediashop.Data.Entity.Discount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DiscountRepository extends JpaRepository<Discount, Long> {
    @Query(value = "SELECT ds FROM Discount ds " +
            "WHERE ds.code =:discountCode " +
            "AND ds.startDate < CURRENT_TIMESTAMP " +
            "AND ds.endDate > CURRENT_TIMESTAMP ")
    Optional<Discount> findByActiveCode(String discountCode);
}
