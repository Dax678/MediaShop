package org.example.mediashop.Data.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Value;
import org.example.mediashop.Data.Entity.Product;

import java.io.Serializable;

/**
 * DTO for {@link Product}
 */
@Value
public class ProductDTO implements Serializable {
    @Positive(message = "Id must be greater than zero")
    Long id;

    @NotNull
    @NotBlank
    String name;

    @NotNull
    @NotBlank
    String description;

    @NotNull
    @NotBlank
    String shortDescription;

    @NotNull
    @NotBlank
    String brand;

    @NotNull
    @NotBlank
    String image;

    @Positive(message = "UnitPrice must be greater than zero")
    @NotNull
    Double unitPrice;

    @Positive(message = "QuantityPerUnit must be greater than zero")
    @NotNull
    Integer quantityPerUnit;

    @NotNull
    Float rating;

    @NotNull
    Boolean isAvailable;
}
