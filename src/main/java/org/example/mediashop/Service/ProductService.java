package org.example.mediashop.Service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.example.mediashop.Configuration.Exception.NotFoundException;
import org.example.mediashop.Data.DTO.Mapper.ProductMapper;
import org.example.mediashop.Data.DTO.ProductDTO;
import org.example.mediashop.Data.Entity.Product;
import org.example.mediashop.Repository.ProductRepository;
import org.example.mediashop.Repository.ProductSpecifications;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.data.domain.Sort.Direction.ASC;

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

    public List<ProductDTO> getProductsByCategoryName(String categoryName,
                                                      String brandName,
                                                      Double minPrice,
                                                      Double maxPrice,
                                                      Double rating,
                                                      Boolean isAvailable,
                                                      Map<String, String> attributes,
                                                      int page,
                                                      int pageSize,
                                                      String pageSortedBy,
                                                      Sort.Direction sortDirection) {
        Map<String, String> attributesMap = new HashMap<>();
        for (Map.Entry<String, String> entry : attributes.entrySet()) {
            if (entry.getKey().startsWith("attribute_"))
                attributesMap.put(entry.getKey().substring(10), entry.getValue());
        }
        Specification<Product> spec = ProductSpecifications.withFilters(categoryName, brandName, minPrice, maxPrice, rating, isAvailable, attributesMap);


        Sort sort;
        if (sortDirection == ASC) {
            sort = Sort.by(pageSortedBy).ascending();
        } else {
            sort = Sort.by(pageSortedBy).descending();
        }

        Pageable pageable = PageRequest.of(page, pageSize, sort);
        Page<Product> productList = productRepository.findAll(spec, pageable);

        if (productList.isEmpty()) {
            logger.warn("Product with category: {} not found", categoryName);
            throw new NotFoundException("Product with category: {0} not found", categoryName);

        }

        return productList.stream()
                .map(ProductMapper::toProductDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public ProductDTO addProduct(final ProductDTO productDTO) {
        Product product = ProductMapper.toProductEntity(productDTO);
        return ProductMapper.toProductDTO(productRepository.save(product));
    }
}
