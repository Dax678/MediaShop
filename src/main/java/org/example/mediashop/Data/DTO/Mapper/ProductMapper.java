package org.example.mediashop.Data.DTO.Mapper;

import org.example.mediashop.Data.DTO.ProductDTO;
import org.example.mediashop.Data.Entity.Product;

public class ProductMapper {
    public static ProductDTO toProductDTO(Product product) {
        return new ProductDTO(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getShortDescription(),
                product.getBrand(),
                product.getImage(),
                product.getUnitPrice(),
                product.getQuantityPerUnit()
        );
    }

    public static Product toProductEntity(ProductDTO productDTO) {
        Product product = new Product();
        product.setId(productDTO.getId());
        product.setName(productDTO.getName());
        product.setDescription(productDTO.getDescription());
        product.setShortDescription(productDTO.getShortDescription());
        product.setBrand(productDTO.getBrand());
        product.setImage(productDTO.getImage());
        product.setUnitPrice(productDTO.getUnitPrice());
        product.setQuantityPerUnit(productDTO.getQuantityPerUnit());
        return product;
    }
}
