package org.example.mediashop.Configuration.Exception;

public class CategoryNotFoundException extends RuntimeException {
    public CategoryNotFoundException() {
        super("Categories not found");
    }

    public CategoryNotFoundException(Long id) {
        super("Category with id " + id + " not found");
    }

    public CategoryNotFoundException(String title) {
        super("Category with title " + title + " not found");
    }
}
