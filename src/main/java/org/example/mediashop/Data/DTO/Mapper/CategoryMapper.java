package org.example.mediashop.Data.DTO.Mapper;

import org.example.mediashop.Data.DTO.CategoryDTO;
import org.example.mediashop.Data.Entity.Category;

public class CategoryMapper {
    public static CategoryDTO toCategoryDTO(Category category) {
        return new CategoryDTO(
                category.getId(),
                category.getParentId(),
                category.getTitle(),
                category.getDescription(),
                category.getMetaTitle(),
                category.getMetaDescription(),
                category.getMetaKeywords(),
                category.getSlug()
        );
    }

    public static Category toCategoryEntity(CategoryDTO categoryDTO) {
        Category category = new Category();
        category.setId(categoryDTO.getId());
        category.setParentId(categoryDTO.getParentId());
        category.setTitle(categoryDTO.getTitle());
        category.setDescription(categoryDTO.getDescription());
        category.setMetaTitle(categoryDTO.getMetaTitle());
        category.setMetaDescription(categoryDTO.getMetaDescription());
        category.setMetaKeywords(categoryDTO.getMetaKeywords());
        category.setSlug(categoryDTO.getSlug());
        return category;
    }
}
