package org.example.mediashop.Service;

import lombok.AllArgsConstructor;
import org.example.mediashop.Data.Entity.Category;
import org.example.mediashop.Repository.CategoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CategoryService {
    private static final Logger logger = LoggerFactory.getLogger(CategoryService.class);

    private final CategoryRepository categoryRepository;

    public ResponseEntity<?> getAllCategories() {
        List<Category> category = categoryRepository.findAll();

        if(!category.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(category);
        } else {
            logger.error("Categories not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Categories not found");
        }
    }

    public ResponseEntity<?> getCategoryById(final Long id) {
            Optional<Category> category = categoryRepository.findCategoryById(id);

            if(category.isPresent()) {
                return ResponseEntity.status(HttpStatus.OK).body(category.get());
            } else {
                logger.warn("Category with id: {} not found", id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product with id: " + id + " not found");
            }
    }

    public ResponseEntity<?> getCategoryByTitle(final String title) {
        Optional<Category> category = categoryRepository.findCategoryByTitle(title);

        if(category.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK).body(category.get());
        } else {
            logger.warn("Category with id: {} not found", title);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product with id: " + title + " not found");
        }
    }

}
