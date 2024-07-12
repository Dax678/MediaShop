package org.example.mediashop.Service;

import lombok.AllArgsConstructor;
import org.example.mediashop.Data.Entity.OrderItem;
import org.example.mediashop.Repository.OrderItemRepository;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class OrderItemService {

    private final OrderItemRepository orderItemRepository;

    public OrderItem addOrderItem(OrderItem orderItem) {
        return orderItemRepository.save(orderItem);
    }

    public double getOrderTotalAmount(Long orderId) {
        return orderItemRepository.getOrderTotalAmount(orderId);
    }
}
