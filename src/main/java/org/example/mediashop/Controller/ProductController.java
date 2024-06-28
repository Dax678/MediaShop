package org.example.mediashop.Controller;

import lombok.AllArgsConstructor;
import org.example.mediashop.Service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@Validated
@RequestMapping(value = "/api/v1/products")
public class ProductController {
    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    private final ProductService productService;

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> getProductById(@PathVariable final Long id) {
        return productService.getProductById(id);
    }

    @GetMapping(value = "/{name}")
    public ResponseEntity<?> getProductByName(@PathVariable final String name) {
        return productService.getProductByName(name);
    }
}
