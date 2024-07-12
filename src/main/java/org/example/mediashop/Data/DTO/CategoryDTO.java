package org.example.mediashop.Data.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Value;
import org.example.mediashop.Data.Entity.Category;

import java.io.Serializable;

/**
 * DTO for {@link Category}
 */
@Value
public class CategoryDTO implements Serializable {
    @Positive(message = "Id must be greater than zero")
    Long id;

    @Positive(message = "Id must be greater than zero")
    Long parentId;

    @NotNull
    @NotBlank
    String title;

    @NotNull
    @NotBlank
    String description;

    @NotNull
    @NotBlank
    String metaTitle;

    @NotNull
    @NotBlank
    String metaDescription;

    @NotNull
    @NotBlank
    String metaKeywords;

    @NotNull
    @NotBlank
    String slug;
}
