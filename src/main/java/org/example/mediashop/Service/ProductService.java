package org.example.mediashop.Service;

import lombok.AllArgsConstructor;
import org.example.mediashop.Configuration.Exception.NotFoundException;
import org.example.mediashop.Data.Entity.Product;
import org.example.mediashop.Repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class ProductService {
    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);

    private final ProductRepository productRepository;

    public Product getProductById(final Long id) {
        Optional<Product> product = productRepository.findProductById(id);

        if (product.isEmpty()) {
            logger.warn("Product with id: {} not found", id);
            throw new NotFoundException("Product with id: {0} not found", id);
        }
        return product.get();
    }

    public Product getProductByName(final String name) {
        Optional<Product> product = productRepository.findProductByName(name);

        if (product.isEmpty()) {
            logger.warn("Product with name: {} not found", name);
            throw new NotFoundException("Product with name: {0} not found", name);
        }

        return product.get();
    }

    public ResponseEntity<?> addProduct(final Product product) {
        Product createdProduct = productRepository.save(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProduct);
    }
}
