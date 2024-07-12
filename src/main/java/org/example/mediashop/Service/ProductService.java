package org.example.mediashop.Service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.example.mediashop.Configuration.Exception.NotFoundException;
import org.example.mediashop.Data.DTO.Mapper.ProductMapper;
import org.example.mediashop.Data.DTO.ProductDTO;
import org.example.mediashop.Data.Entity.Product;
import org.example.mediashop.Repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class ProductService {
    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);

    private final ProductRepository productRepository;

    public ProductDTO getProductById(final Long id) {
        Optional<Product> product = productRepository.findProductById(id);

        if (product.isEmpty()) {
            logger.warn("Product with id: {} not found", id);
            throw new NotFoundException("Product with id: {0} not found", id);
        }
        return ProductMapper.toProductDTO(product.get());
    }

    public ProductDTO getProductByName(final String name) {
        Optional<Product> product = productRepository.findProductByName(name);

        if (product.isEmpty()) {
            logger.warn("Product with name: {} not found", name);
            throw new NotFoundException("Product with name: {0} not found", name);
        }

        return ProductMapper.toProductDTO(product.get());
    }

    @Transactional
    public ProductDTO addProduct(final ProductDTO productDTO) {
        Product product = ProductMapper.toProductEntity(productDTO);
        return ProductMapper.toProductDTO(productRepository.save(product));
    }
}
