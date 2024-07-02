package org.example.mediashop.Service;

import org.example.mediashop.Configuration.Exception.ProductNotFoundException;
import org.example.mediashop.Data.Entity.Product;
import org.example.mediashop.Repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    List<Product> expectedProducts = new ArrayList<>();

    static final String EXPECTED_PRODUCT_NAME = "Smartphone X";
    static final String NONEXISTENT_PRODUCT_NAME = "Nonexistent Product";
    static final long EXPECTED_PRODUCT_ID = 1L;
    static final String EXPECTED_PRODUCT_ID_EXCEPTION_MESSAGE = "Product with id " + EXPECTED_PRODUCT_ID + " not found";
    static final String EXPECTED_PRODUCT_NAME_EXCEPTION_MESSAGE = "Product with name " + NONEXISTENT_PRODUCT_NAME + " not found";


    @BeforeEach
    void setUp() {
        Mockito.reset(productRepository);

        expectedProducts = List.of(
                new Product(1L, "Smartphone X", "Lorem ipsum dolor sit amet, consectetur adipiscing elit.", "Smartphone X", "X Brand", "image1.jpg", 100.0, 10, null, null, null),
                new Product(2L, "Smartphone Y", "Lorem ipsum dolor sit amet, consectetur adipiscing elit.", "Smartphone Y", "Y Brand", "image2.jpg", 200.0, 20, null, null, null),
                new Product(3L, "Smartphone Z", "Lorem ipsum dolor sit amet, consectetur adipiscing elit.", "Smartphone Z", "Z Brand", "image3.jpg", 300.0, 30, null, null, null)
        );
    }

    @Test
    void testGetProductById_WhenProductExists_ShouldReturnCategory() {
        // Given
        Mockito.when(productRepository.findProductById(anyLong())).thenReturn(Optional.ofNullable(expectedProducts.get(0)));

        // When
        Product result = productService.getProductById(1L);

        // Then
        assertNotNull(result);
        assertEquals(expectedProducts.get(0), result);
    }

    @Test
    void testGetProductById_WhenProductDoesNotExists_ShouldThrowProductNotFoundException() {
        // Given
        Mockito.when(productRepository.findProductById(anyLong())).thenReturn(Optional.empty());

        // When
        ProductNotFoundException exception = assertThrows(ProductNotFoundException.class, () -> productService.getProductById(EXPECTED_PRODUCT_ID));

        // Then
        assertEquals(exception.getMessage(), EXPECTED_PRODUCT_ID_EXCEPTION_MESSAGE);
    }

    @Test
    void testGetProductByName_WhenProductExists_ShouldReturnCategory() {
        // Given
        Mockito.when(productRepository.findProductByName(anyString())).thenReturn(Optional.ofNullable(expectedProducts.get(0)));

        // When
        Product result = productService.getProductByName(EXPECTED_PRODUCT_NAME);

        // Then
        assertNotNull(result);
        assertEquals(expectedProducts.get(0), result);
    }

    @Test
    void testGetProductByName_WhenProductDoesNotExists_ShouldThrowProductNotFoundException() {
        // Given
        Mockito.when(productRepository.findProductByName(anyString())).thenReturn(Optional.empty());

        // When
        ProductNotFoundException exception = assertThrows(ProductNotFoundException.class, () -> productService.getProductByName(NONEXISTENT_PRODUCT_NAME));

        // Then
        assertEquals(exception.getMessage(), EXPECTED_PRODUCT_NAME_EXCEPTION_MESSAGE);
    }
}