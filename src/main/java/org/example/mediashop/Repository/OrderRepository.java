package org.example.mediashop.Repository;

import org.example.mediashop.Data.Entity.Order;
import org.example.mediashop.Data.Entity.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    Optional<Order> findOrderById(Long id);

    List<Order> findOrderByStatus(OrderStatus status);

    List<Order> findOrderByStatusAndUser_Id(OrderStatus status, Long user_id);
    boolean existsById(Long id);
}
