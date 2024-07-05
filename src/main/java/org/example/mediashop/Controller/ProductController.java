package org.example.mediashop.Controller;

import jakarta.validation.ConstraintViolationException;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import org.example.mediashop.Data.Entity.Product;
import org.example.mediashop.Service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@AllArgsConstructor
@Validated
@RequestMapping(value = "/api/v1/products")
public class ProductController {
    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    private final ProductService productService;

    /**
     * Retrieves a product by its unique identifier.
     *
     * @param id The unique identifier of the product. This parameter is expected to be a positive integer.
     * @return A ResponseEntity containing a map with a single entry, where the key is "product" and the value is the retrieved product.
     * @throws ConstraintViolationException If the provided id is not a positive integer.
     */
    @GetMapping(value = "/id/{id}")
    public ResponseEntity<Map<String, Product>> getProductById(@PathVariable("id") @Positive final Long id) {
        Product product = productService.getProductById(id);
        return ResponseEntity.status(HttpStatus.OK).body(Map.of("product", product));
    }

    /**
     * Retrieves a product by its name.
     *
     * @param name The name of the product. This parameter is expected to be a non-empty string.
     * @return A ResponseEntity containing a map with a single entry, where the key is "product" and the value is the retrieved product.
     * @throws ConstraintViolationException If the provided name is not a non-empty string.
     */
    @GetMapping(value = "/name/{name}")
    public ResponseEntity<Map<String, Product>> getProductByName(@PathVariable("name") @NotBlank final String name) {
        Product product = productService.getProductByName(name);
        return ResponseEntity.status(HttpStatus.OK).body(Map.of("product", product));
    }
}
