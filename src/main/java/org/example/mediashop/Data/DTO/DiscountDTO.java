package org.example.mediashop.Data.DTO;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.example.mediashop.Data.Entity.Discount;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO for {@link Discount}
 */
@Value
public class DiscountDTO implements Serializable {
    @Positive(message = "Id must be greater than zero")
    Long id;

    @Positive(message = "Id must be greater than zero")
    @NotNull
    Double value;

    @NotNull
    String code;

    @DateTimeFormat
    LocalDateTime startDate;

    @DateTimeFormat
    LocalDateTime endDate;

    @Positive(message = "MaxUsage must be greater than zero")
    @NotNull
    int maxUsage;

    @NotNull
    Double minPurchaseAmount;
}
