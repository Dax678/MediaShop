package org.example.mediashop.Service;

import lombok.AllArgsConstructor;
import org.example.mediashop.Configuration.Exception.CategoryNotFoundException;
import org.example.mediashop.Data.Entity.Category;
import org.example.mediashop.Repository.CategoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CategoryService {
    private static final Logger logger = LoggerFactory.getLogger(CategoryService.class);

    private final CategoryRepository categoryRepository;

    public List<Category> getAllCategories() {
        List<Category> category = categoryRepository.findAll();

        if(category.isEmpty()) {
            logger.error("Categories not found");
            throw new CategoryNotFoundException();
        }
        return category;
    }

    public Category getCategoryById(final Long id) {
            Optional<Category> category = categoryRepository.findCategoryById(id);

            if(category.isEmpty()) {
                logger.warn("Category with id: {} not found", id);
                throw new CategoryNotFoundException(id);
            }
            return category.get();
    }

    public Category getCategoryByTitle(final String title) {
        Optional<Category> category = categoryRepository.findCategoryByTitle(title);

        if(category.isEmpty()) {
            logger.warn("Category with title: {} not found", title);
            throw new CategoryNotFoundException(title);
        }
        return category.get();
    }

}
