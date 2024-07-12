package org.example.mediashop.Service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.example.mediashop.Configuration.Exception.NotFoundException;
import org.example.mediashop.Configuration.Exception.NotModifiedException;
import org.example.mediashop.Data.DTO.Mapper.OrderMapper;
import org.example.mediashop.Data.DTO.OrderDTO;
import org.example.mediashop.Data.DTO.ProductDTO;
import org.example.mediashop.Data.Entity.Order;
import org.example.mediashop.Data.Entity.OrderStatus;
import org.example.mediashop.Repository.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class OrderService {
    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);

    private final OrderRepository orderRepository;

    private final ProductService productService;

    private final OrderItemService orderItemService;

    private final DiscountService discountService;

    private final UserService userService;

    public OrderDTO getOrderById(Long id) {
        Optional<Order> order = orderRepository.findOrderById(id);

        if (order.isEmpty()) {
            logger.warn("Order with id: {} not found", id);
            throw new NotFoundException("Order with id: {0} not found", id);
        }

        return OrderMapper.toOrderDTO(order.get());
    }

    public List<OrderDTO> getOrderByStatus(String status) {
        List<Order> order = orderRepository.findOrderByStatus(OrderStatus.valueOf(status));

        if (order.isEmpty()) {
            logger.warn("Order with status: {} not found", status);
            throw new NotFoundException("Order with status: {0} not found", status);
        }

        return order.stream()
                .map(OrderMapper::toOrderDTO)
                .collect(Collectors.toList());
    }

    public List<OrderDTO> getOrderByStatusAndUserId(String status, Long userId) {
        List<Order> order = orderRepository.findOrderByStatusAndUser_Id(OrderStatus.valueOf(status), userId);

        if (order.isEmpty()) {
            logger.warn("Order with status: {} and user id: {} not found", status, userId);
            throw new NotFoundException("Order with status: {0} and user id: {1} not found", status, userId);
        }

        return order.stream()
                .map(OrderMapper::toOrderDTO)
                .collect(Collectors.toList());
    }

    public OrderDTO updateOrderStatusById(Long id, String status) {
        Optional<Order> order = orderRepository.findOrderById(id);

        if (order.isEmpty()) {
            logger.warn("Order with id: {} not found", id);
            throw new NotFoundException("Order with id: {0} not found", id);
        }

        order.get().setStatus(OrderStatus.valueOf(status));
        Order updatedOrder = orderRepository.save(order.get());

        if (!updatedOrder.getStatus().equals(OrderStatus.valueOf(status))) {
            logger.error("Failed to update order status with id: {} to {}. Order has not been updated.", id, status);
            throw new NotModifiedException("Failed to update order status with id: {0} to {1}", id, status);
        }

        return OrderMapper.toOrderDTO(updatedOrder);
    }

    @Transactional
    public OrderDTO createOrder(OrderDTO orderDTO) {
        Order order = OrderMapper.toOrderEntity(orderDTO);

        if(!userService.checkIfUserExists(order.getUserId())) {
            logger.warn("User with id: {} not found", order.getUserId());
            throw new NotFoundException("User with id: {0} not found", order.getUserId());
        }

        Order savedOrder = orderRepository.save(order);

        order.getProducts()
                .forEach(orderItem -> {
                    ProductDTO productDTO = productService.getProductById(orderItem.getProductId());
                    Double discountValue = orderItem.getDiscountId() != null ? discountService.getDiscountValueById(orderItem.getDiscountId()) : 0.0;

                    orderItem.setOrderId(savedOrder.getId());
                    orderItem.setProductId(productDTO.getId());

                    orderItem.setPrice(productDTO.getUnitPrice() - discountValue);

                    orderItemService.addOrderItem(orderItem);
                });

        order.setTotalAmount(orderItemService.getOrderTotalAmount(savedOrder.getId()));

        return getOrderById(savedOrder.getId());
    }
}
