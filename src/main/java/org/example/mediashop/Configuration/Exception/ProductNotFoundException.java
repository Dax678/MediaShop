package org.example.mediashop.Configuration.Exception;

public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException() {
        super("Products not found");
    }

    public ProductNotFoundException(Long id) {
        super("Product with id " + id + " not found");
    }

    public ProductNotFoundException(String title) {
        super("Product with name " + title + " not found");
    }
}
