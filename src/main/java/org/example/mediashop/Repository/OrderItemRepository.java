package org.example.mediashop.Repository;

import org.example.mediashop.Data.Entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    @Query("SELECT SUM(price) FROM OrderItem WHERE orderId =:orderId")
    double getOrderTotalAmount(Long orderId);
}
