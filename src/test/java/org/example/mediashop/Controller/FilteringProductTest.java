package org.example.mediashop.Controller;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.example.mediashop.Data.Entity.*;
import org.example.mediashop.Repository.CategoryRepository;
import org.example.mediashop.Repository.ProductCategoryRepository;
import org.example.mediashop.Repository.ProductRepository;
import org.example.mediashop.Repository.UserRepository;
import org.example.mediashop.TestConfig.IntegrationTest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.testcontainers.shaded.com.fasterxml.jackson.core.JsonProcessingException;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

public class FilteringProductTest extends IntegrationTest {

    @LocalServerPort
    private Integer port;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductCategoryRepository productCategoryRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeAll
    static void beforeAll() {
        postgres.start();
    }

    @AfterAll
    static void afterAll() {
        postgres.stop();
    }

    List<Category> categoryList;
    List<Product> productList;
    List<ProductCategory> productCategories;

    User user;
    String token;

    @BeforeEach
    void setUp() throws JsonProcessingException {
        RestAssured.baseURI = "http://localhost:" + port;
        productCategoryRepository.truncateAllData();
        userRepository.deleteAll();

        categoryList = new ArrayList<>();
        categoryList.add(new Category(null, "CategoryTitle", "CategoryDescription", "MetaTitle", "MetaDescription", "new, category", "new-category-1", null, null, null));
        categoryList.add(new Category(null, "CategoryTitle2", "CategoryDescription2", "MetaTitle2", "MetaDescription2", "new, category", "new-category-2", null, null, null));

        categoryRepository.saveAll(categoryList);

        productList = new ArrayList<>();
        productList.add(new Product("ProductName1", "Lorem ipsum dolor sit amet, consectetur adipiscing elit.", "ShortDescription", "BrandName", "image1.jpg", 100.0, 1, 5.0F, true));
        productList.add(new Product("ProductName2", "Lorem ipsum dolor sit amet, consectetur adipiscing elit.", "ShortDescription", "BrandName", "image2.jpg", 200.0, 2, 4.0F, false));
        productList.add(new Product("ProductName3", "Lorem ipsum dolor sit amet, consectetur adipiscing elit.", "ShortDescription", "BrandName", "image3.jpg", 300.0, 3, 5.0F, true));
        productList.add(new Product("ProductName4", "Lorem ipsum dolor sit amet, consectetur adipiscing elit.", "ShortDescription", "BrandName", "image4.jpg", 400.0, 4, 4.0F, false));
        productList.add(new Product("ProductName5", "Lorem ipsum dolor sit amet, consectetur adipiscing elit.", "ShortDescription", "BrandName", "image4.jpg", 500.0, 5, 5.0F, true));

        productRepository.saveAll(productList);

        productCategories = new ArrayList<>();
        productCategories.add(new ProductCategory(new ProductCategoryKey(productList.get(0).getId(), categoryList.get(0).getId()), productList.get(0), categoryList.get(0)));
        productCategories.add(new ProductCategory(new ProductCategoryKey(productList.get(1).getId(), categoryList.get(0).getId()), productList.get(1), categoryList.get(0)));
        productCategories.add(new ProductCategory(new ProductCategoryKey(productList.get(2).getId(), categoryList.get(1).getId()), productList.get(2), categoryList.get(1)));
        productCategories.add(new ProductCategory(new ProductCategoryKey(productList.get(3).getId(), categoryList.get(1).getId()), productList.get(3), categoryList.get(1)));
        productCategories.add(new ProductCategory(new ProductCategoryKey(productList.get(4).getId(), categoryList.get(1).getId()), productList.get(4), categoryList.get(1)));

        productCategoryRepository.saveAll(productCategories);

        user = userRepository.save(new User("userTest", passwordEncoder.encode("passwordTest"), "email@test.com", "ROLE_USER"));
        userRepository.save(new User("adminTest", passwordEncoder.encode("passwordTest"), "email.admin@test.com", "ROLE_ADMIN"));

        token = given()
                .contentType(ContentType.JSON)
                .when()
                .body(new ObjectMapper().writeValueAsString(Map.of("username", "userTest", "password", "passwordTest")))
                .post("/api/v1/auth/login")
                .then()
                .extract().path("token");
    }

    @Test
    void getProductsByCategoryNameFilter_shouldReturn200_whenNoFilterSelected() {
        String categoryTitle = "CategoryTitle";
        List<Product> products = productList.subList(0, 2);

        given()
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .when()
                //.pathParams("")
                .get("/api/v1/products/category/{0}", categoryTitle)
                .then()
                .statusCode(200)
                .body("product.id", is(
                        products.stream()
                                .map(product -> product.getId().intValue())
                                .collect(Collectors.toList()))
                )
                .body("product.name", is(
                        products.stream()
                                .map(Product::getName)
                                .collect(Collectors.toList()))
                )
                .body("product.description", is(
                        products.stream()
                                .map(Product::getDescription)
                                .collect(Collectors.toList()))
                )
                .body("product.shortDescription", is(
                        products.stream()
                                .map(Product::getShortDescription)
                                .collect(Collectors.toList()))
                )
                .body("product.brand", is(
                        products.stream()
                                .map(Product::getBrand)
                                .collect(Collectors.toList()))
                )
                .body("product.image", is(
                        products.stream()
                                .map(Product::getImage)
                                .collect(Collectors.toList()))
                )
                .body("product.unitPrice", is(
                        products.stream()
                                .map(product -> product.getUnitPrice().floatValue())
                                .collect(Collectors.toList()))
                )
                .body("product.quantityPerUnit", is(
                        products.stream()
                                .map(Product::getQuantityPerUnit)
                                .collect(Collectors.toList()))
                )
                .body("product.rating", is(
                        products.stream()
                                .map(Product::getRating)
                                .collect(Collectors.toList()))
                )
                .body("product.isAvailable", is(
                        products.stream()
                                .map(Product::getIsAvailable)
                                .collect(Collectors.toList()))
                );
    }

    @Test
    void getProductsByCategoryNameFilter_shouldReturnSortedListByUnitPriceDesc() {
        String categoryTitle = "CategoryTitle2";
        List<Product> products = productList.subList(3, 5).stream()
                .sorted(Comparator.comparing(Product::getUnitPrice).reversed())
                .toList();

        given()
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .when()
                .get("/api/v1/products/category/{categoryTitle}?page={page}&size={size}&sortedBy={sortedBy}&sortDirection={sortDirection}",
                        categoryTitle, 0, 2, "unitPrice", "DESC")
                .then()
                .statusCode(200)
                .body("product.id", is(
                        products.stream()
                                .map(product -> product.getId().intValue())
                                .collect(Collectors.toList()))
                )
                .body("product.name", is(
                        products.stream()
                                .map(Product::getName)
                                .collect(Collectors.toList()))
                )
                .body("product.description", is(
                        products.stream()
                                .map(Product::getDescription)
                                .collect(Collectors.toList()))
                )
                .body("product.shortDescription", is(
                        products.stream()
                                .map(Product::getShortDescription)
                                .collect(Collectors.toList()))
                )
                .body("product.brand", is(
                        products.stream()
                                .map(Product::getBrand)
                                .collect(Collectors.toList()))
                )
                .body("product.image", is(
                        products.stream()
                                .map(Product::getImage)
                                .collect(Collectors.toList()))
                )
                .body("product.unitPrice", is(
                        products.stream()
                                .map(product -> product.getUnitPrice().floatValue())
                                .collect(Collectors.toList()))
                )
                .body("product.quantityPerUnit", is(
                        products.stream()
                                .map(Product::getQuantityPerUnit)
                                .collect(Collectors.toList()))
                )
                .body("product.rating", is(
                        products.stream()
                                .map(Product::getRating)
                                .collect(Collectors.toList()))
                )
                .body("product.isAvailable", is(
                        products.stream()
                                .map(Product::getIsAvailable)
                                .collect(Collectors.toList())
                ));
    }

    @Test
    void getProductsByCategoryNameFilter_shouldReturn404_whenProductNotFound() {
        String categoryTitle = "CategoryTitle3";

        given()
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .when()
                .get("/api/v1/products/category/{0}", categoryTitle)
                .then()
                .statusCode(404)
                .body("message", is("Product with category: " + categoryTitle + " not found"))
                .body("statusCode", is(404));
    }

    @Test
    void getProductsByCategoryNameFilter_shouldReturn400_whenCategoryIsInvalid() {
        given()
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .when()
                .get("/api/v1/products/category/{0}", " ")
                .then()
                .statusCode(400)
                .body("message", is("getProductsByCategoryNameFilter.categoryName: must not be blank"))
                .body("statusCode", is(400));
    }
}
