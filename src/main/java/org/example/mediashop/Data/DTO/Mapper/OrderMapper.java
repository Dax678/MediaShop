package org.example.mediashop.Data.DTO.Mapper;

import org.example.mediashop.Data.DTO.OrderDTO;
import org.example.mediashop.Data.DTO.OrderItemDTO;
import org.example.mediashop.Data.Entity.Order;
import org.example.mediashop.Data.Entity.OrderItem;
import org.example.mediashop.Data.Entity.OrderPaymentStatus;
import org.example.mediashop.Data.Entity.OrderStatus;

import java.util.List;
import java.util.stream.Collectors;

public class OrderMapper {

    public static OrderDTO toOrderDTO(Order order) {
        List<OrderItemDTO> orderItemDTOs = order.getProducts().stream()
                .map(OrderMapper::toOrderItemDTO)
                .collect(Collectors.toList());

        return new OrderDTO(
                order.getId(),
                order.getUserId(),
                orderItemDTOs,
                order.getTotalAmount(),
                order.getStatus().name(),
                order.getPaymentStatus().name(),
                order.getPaymentMethod(),
                order.getShippingAddress(),
                order.getShippingMethod(),
                order.getOrderDate(),
                order.getDeliveryDate()
        );
    }

    public static OrderItemDTO toOrderItemDTO(OrderItem orderItem) {
        return new OrderItemDTO(
                orderItem.getId(),
                orderItem.getProductId(),
                orderItem.getDiscountId(),
                orderItem.getPrice()
        );
    }

    public static Order toOrderEntity(OrderDTO orderDTO) {
        List<OrderItem> orderItems = orderDTO.getProducts().stream()
                .map(OrderMapper::toOrderItemEntity)
                .collect(Collectors.toList());

        Order order = new Order();
        order.setId(orderDTO.getId());
        order.setUserId(orderDTO.getUserId());
        order.setProducts(orderItems);

        if(orderDTO.getTotalAmount() == null) {
            order.setTotalAmount(-1.0);
        } else {
            order.setTotalAmount(orderDTO.getTotalAmount());
        }

        order.setStatus(OrderStatus.valueOf(orderDTO.getStatus()));
        order.setPaymentStatus(OrderPaymentStatus.valueOf(orderDTO.getPaymentStatus()));
        order.setPaymentMethod(orderDTO.getPaymentMethod());
        order.setShippingAddress(orderDTO.getShippingAddress());
        order.setShippingMethod(orderDTO.getShippingMethod());
        order.setOrderDate(orderDTO.getOrderDate());
        order.setDeliveryDate(orderDTO.getDeliveryDate());

        return order;
    }

    public static OrderItem toOrderItemEntity(OrderItemDTO orderItemDTO) {
        OrderItem orderItem = new OrderItem();
        orderItem.setId(orderItemDTO.getId());
        orderItem.setProductId(orderItemDTO.getProductId());
        orderItem.setDiscountId(orderItemDTO.getDiscountId());
        orderItem.setPrice(orderItemDTO.getPrice());
        return orderItem;
    }
}
