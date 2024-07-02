package org.example.mediashop.Controller;

import lombok.AllArgsConstructor;
import org.example.mediashop.Data.Entity.Category;
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

    @GetMapping
    public ResponseEntity<Map<String, List<Category>>> getAllCategories() {
        List<Category> category = categoryService.getAllCategories();
        return ResponseEntity.status(HttpStatus.OK).body(Map.of("category", category));
    }

    @GetMapping(value = "/id/{id}")
    public ResponseEntity<Map<String, Category>> getCategoryById(@PathVariable(value = "id") final Long id) {
        Category category = categoryService.getCategoryById(id);
        return ResponseEntity.status(HttpStatus.OK).body(Map.of("category", category));
    }

    @GetMapping(value = "/title/{title}")
    public ResponseEntity<Map<String, Category>> getCategoryByTitle(@PathVariable(value = "title") final String title) {
        Category category = categoryService.getCategoryByTitle(title);

        return ResponseEntity.status(HttpStatus.OK).body(Map.of("category", category));
    }
}
