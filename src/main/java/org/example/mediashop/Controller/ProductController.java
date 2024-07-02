package org.example.mediashop.Controller;

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

    @GetMapping(value = "/id/{id}")
    public ResponseEntity<Map<String, Product>> getProductById(@PathVariable final Long id) {
        Product product = productService.getProductById(id);
        return ResponseEntity.status(HttpStatus.OK).body(Map.of("product", product));
    }

    @GetMapping(value = "/name/{name}")
    public ResponseEntity<Map<String, Product>> getProductByName(@PathVariable final String name) {
        Product product = productService.getProductByName(name);
        return ResponseEntity.status(HttpStatus.OK).body(Map.of("product", product));
    }
}
