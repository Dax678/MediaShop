package org.example.mediashop.Data.DTO;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Positive;
import lombok.Value;
import org.example.mediashop.Configuration.Exception.ValueOfEnum;
import org.example.mediashop.Data.Entity.Order;
import org.example.mediashop.Data.Entity.OrderPaymentStatus;
import org.example.mediashop.Data.Entity.OrderStatus;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO for {@link Order}
 */
@Value
public class OrderDTO implements Serializable {
    @Positive(message = "Id must be greater than zero")
    Long id;

    @Positive(message = "UserId must be greater than zero")
    Long userId;

    List<OrderItemDTO> products;

    Double totalAmount;

    @NotNull(message = "Status can not be null")
    @ValueOfEnum(enumClass = OrderStatus.class, message = "Status must be a valid OrderStatus")
    String status;

    @NotNull(message = "Payment Status can not be null")
    @ValueOfEnum(enumClass = OrderPaymentStatus.class, message = "Payment status must be a valid OrderPaymentStatus")
    String paymentStatus;

    @NotNull(message = "Payment Method can not be null")
    String paymentMethod;

    @NotNull(message = "Shipping Address can not be null")
    String shippingAddress;

    @NotNull(message = "Shipping Method can not be null")
    String shippingMethod;

    @NotNull(message = "Order Date can not be null")
    @Past(message = "Order Date must be in the past")
    LocalDateTime orderDate;

    @NotNull(message = "Delivery Date can not be null")
    @Future(message = "Order Date must be in the future")
    LocalDateTime deliveryDate;
}