package org.example.mediashop.Controller;

import jakarta.validation.ConstraintViolationException;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import org.example.mediashop.Data.DTO.CategoryDTO;
import org.example.mediashop.Service.CategoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
@Validated
@RequestMapping(value = "/api/v1/categories")
public class CategoryController {
    private static final Logger logger = LoggerFactory.getLogger(CategoryController.class);

    private final CategoryService categoryService;

    /**
     * This method retrieves all categories from the database.
     *
     * @return ResponseEntity containing a map with a key "category" and a list of Category objects as its value.
     */
    @GetMapping
    public ResponseEntity<Map<String, List<CategoryDTO>>> getAllCategories() {
        List<CategoryDTO> category = categoryService.getAllCategories();
        return ResponseEntity.status(HttpStatus.OK).body(Map.of("category", category));
    }

    /**
     * This method retrieves a category from the database based on its unique identifier.
     *
     * @param id The unique identifier of the category to retrieve.
     * @return ResponseEntity containing a map with a key "category" and a Category object as its value.
     * @throws ConstraintViolationException If the provided id is not a positive number.
     */
    @GetMapping(value = "/id/{id}")
    public ResponseEntity<Map<String, CategoryDTO>> getCategoryById(@PathVariable(value = "id") @Positive final Long id) {
        CategoryDTO category = categoryService.getCategoryById(id);
        return ResponseEntity.status(HttpStatus.OK).body(Map.of("category", category));
    }

    /**
     * This method retrieves a category from the database based on its title.
     *
     * @param title The title of the category to retrieve.
     * @return ResponseEntity containing a map with a key "category" and a Category object as its value.
     * @throws ConstraintViolationException If the provided title is not a non-empty string.
     */
    @GetMapping(value = "/title/{title}")
    public ResponseEntity<Map<String, CategoryDTO>> getCategoryByTitle(@PathVariable(value = "title") @NotBlank final String title) {
        CategoryDTO category = categoryService.getCategoryByTitle(title);

        return ResponseEntity.status(HttpStatus.OK).body(Map.of("category", category));
    }
}
