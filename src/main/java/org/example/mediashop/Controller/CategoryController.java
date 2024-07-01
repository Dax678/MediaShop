package org.example.mediashop.Controller;

import lombok.AllArgsConstructor;
import org.example.mediashop.Service.CategoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@Validated
@RequestMapping(value = "/api/v1/categories")
public class CategoryController {
    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    private final CategoryService categoryService;

    @GetMapping
    public ResponseEntity<?> getAllCategories() {
        return categoryService.getAllCategories();
    }

    @GetMapping(value = "/id")
    public ResponseEntity<?> getCategoryById(@RequestParam(value = "id") final Long id) {
        return categoryService.getCategoryById(id);
    }

    @GetMapping(value = "/title")
    public ResponseEntity<?> getCategoryByTitle(@RequestParam(value = "title") final String title) {
        return categoryService.getCategoryByTitle(title);
    }
}
