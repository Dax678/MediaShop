package org.example.mediashop.Service;

import lombok.AllArgsConstructor;
import org.example.mediashop.Configuration.Exception.NotFoundException;
import org.example.mediashop.Configuration.Exception.NotModifiedException;
import org.example.mediashop.Data.Entity.Order;
import org.example.mediashop.Data.Entity.OrderStatus;
import org.example.mediashop.Repository.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class OrderService {
    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);

    private final OrderRepository orderRepository;

    public Order getOrderById(Long id) {
        Optional<Order> order = orderRepository.findOrderById(id);

        if (order.isEmpty()) {
            logger.warn("Order with id: {} not found", id);
            throw new NotFoundException("Order with id: {0} not found", id);
        }

        return order.get();
    }

    public List<Order> getOrderByStatus(String status) {
        List<Order> order = orderRepository.findOrderByStatus(OrderStatus.valueOf(status));

        if (order.isEmpty()) {
            logger.warn("Order with status: {} not found", status);
            throw new NotFoundException("Order with status: {0} not found", status);
        }

        return order;
    }

    public List<Order> getOrderByStatusAndUserId(String status, Long userId) {
        List<Order> order = orderRepository.findOrderByStatusAndUser_Id(OrderStatus.valueOf(status), userId);

        if (order.isEmpty()) {
            logger.warn("Order with status: {} and user id: {} not found", status, userId);
            throw new NotFoundException("Order with status: {0} and user id: {1} not found", status, userId);
        }

        return order;
    }

    public int updateOrderStatusById(Long id, String status) {
        if(!orderRepository.existsById(id)) {
            logger.warn("Order with id: {} not found", id);
            throw new NotFoundException("Order with id: {0} not found", id);
        }

        int rowsAffected = orderRepository.updateOrderStatusById(OrderStatus.valueOf(status), id);

        if (rowsAffected == 0) {
            logger.error("Failed to update order status with id: {} to {}", id, status);
            throw new NotModifiedException(MessageFormat.format("Failed to update order status with id: {0} to {1}", id, status));
        }

        return rowsAffected;
    }
}