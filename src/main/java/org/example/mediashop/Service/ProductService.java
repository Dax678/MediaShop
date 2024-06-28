package org.example.mediashop.Service;

import lombok.AllArgsConstructor;
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

    public ResponseEntity<?> getProductById(final Long id) {
        Optional<Product> product = productRepository.findProductById(id);

        if(product.isPresent())
            return ResponseEntity.status(HttpStatus.OK).body(product.get());
        else {
            logger.warn("Product with id: {} not found", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product with id: " + id + " not found");
        }
    }

    public ResponseEntity<?> getProductByName(final String name) {
        Optional<Product> product = productRepository.findProductByName(name);

        if(product.isPresent())
            return ResponseEntity.status(HttpStatus.OK).body(product.get());
        else {
            logger.warn("Product with name: {} not found", name);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product with name: " + name + " not found");
        }
    }
}
