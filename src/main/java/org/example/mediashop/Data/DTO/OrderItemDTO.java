package org.example.mediashop.Data.DTO;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Value;
import org.example.mediashop.Data.Entity.OrderItem;

import java.io.Serializable;

/**
 * DTO for {@link OrderItem}
 */
@Value
public class OrderItemDTO implements Serializable {
    @NotNull
    @Positive(message = "Id must be greater than zero")
    Long id;

    @NotNull(message = "ProductId can not be null")
    @Positive(message = "ProductId must be greater than zero")
    Long productId;

    @Positive(message = "DiscountId must be greater than zero")
    Long discountId;

    @NotNull
    @Positive(message = "Price must be greater than zero")
    double price;
}