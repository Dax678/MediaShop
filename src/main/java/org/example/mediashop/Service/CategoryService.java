package org.example.mediashop.Service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.example.mediashop.Configuration.Exception.NotFoundException;
import org.example.mediashop.Data.DTO.CategoryDTO;
import org.example.mediashop.Data.DTO.Mapper.CategoryMapper;
import org.example.mediashop.Data.Entity.Category;
import org.example.mediashop.Repository.CategoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CategoryService {
    private static final Logger logger = LoggerFactory.getLogger(CategoryService.class);

    private final CategoryRepository categoryRepository;

    public List<CategoryDTO> getAllCategories() {
        List<Category> category = categoryRepository.findAll();

        if (category.isEmpty()) {
            logger.error("Categories not found");
            throw new NotFoundException("Categories not found");
        }
        return category.stream()
                .map(CategoryMapper::toCategoryDTO)
                .collect(Collectors.toList());
    }

    public CategoryDTO getCategoryById(final Long id) {
        Optional<Category> category = categoryRepository.findCategoryById(id);

        if (category.isEmpty()) {
            logger.warn("Category with id: {} not found", id);
            throw new NotFoundException("Category with id: {0} not found", id);
        }
        return CategoryMapper.toCategoryDTO(category.get());
    }

    public CategoryDTO getCategoryByTitle(final String title) {
        Optional<Category> category = categoryRepository.findCategoryByTitle(title);

        if (category.isEmpty()) {
            logger.warn("Category with title: {} not found", title);
            throw new NotFoundException("Category with title: {0} not found", title);
        }
        return CategoryMapper.toCategoryDTO(category.get());
    }

    public List<CategoryDTO> getCategoriesByParentId(final Long parentId) {
        List<Category> category = categoryRepository.findCategoriesByParentId(parentId);

        if (category.isEmpty()) {
            logger.warn("Category with parentId: {} not found", parentId);
            throw new NotFoundException("Category with parentId: {0} not found", parentId);
        }
        return category.stream()
                .map(CategoryMapper::toCategoryDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public CategoryDTO addCategory(CategoryDTO categoryDTO) {
        Category category = CategoryMapper.toCategoryEntity(categoryDTO);
        return CategoryMapper.toCategoryDTO(categoryRepository.save(category));
    }
}
