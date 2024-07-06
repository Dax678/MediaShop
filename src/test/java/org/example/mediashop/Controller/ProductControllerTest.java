package org.example.mediashop.Controller;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.example.mediashop.Data.Entity.Product;
import org.example.mediashop.Repository.ProductRepository;
import org.example.mediashop.TestConfig.IntegrationTest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.server.LocalServerPort;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

class ProductControllerTest extends IntegrationTest {

    @LocalServerPort
    private Integer port;

    @Autowired
    private ProductRepository productRepository;

    @BeforeAll
    static void beforeAll() {
        postgres.start();
    }

    @AfterAll
    static void afterAll() {
        postgres.stop();
    }

    @BeforeEach
    void setUp() {
        RestAssured.baseURI = "http://localhost:" + port;
        productRepository.deleteAll();
    }

    @Test
    void shouldGetProductById() {
        Product product = productRepository.save(
                new Product("ProductName1", "Lorem ipsum dolor sit amet, consectetur adipiscing elit.", "ShortDescription", "BrandName", "image1.jpg", 100.0, 10, null, null, null)
        );

        // Given
        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/api/v1/products/id/{0}", product.getId().intValue())
                .then()
                .statusCode(200)
                .body("product.id", is(product.getId().intValue()))
                .body("product.name", is(product.getName()))
                .body("product.description", is(product.getDescription()))
                .body("product.shortDescription", is(product.getShortDescription()))
                .body("product.brand", is(product.getBrand()))
                .body("product.image", is(product.getImage()))
                .body("product.unitPrice", is(product.getUnitPrice().floatValue()))
                .body("product.quantityPerUnit", is(product.getQuantityPerUnit()));
    }

    @Test
    void getProductById_shouldReturn400_whenProductIdIsInvalid() {
        long productId = 0L;

        // Given
        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/api/v1/products/id/{0}", productId)
                .then()
                .statusCode(400)
                .body("message", is("Validation error: getProductById.id: must be greater than " + productId))
                .body("statusCode", is(400));
    }

    @Test
    void getProductById_shouldReturn404_whenProductIdNotFound() {
        long productId = 10L;

        // Given
        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/api/v1/products/id/{0}", productId)
                .then()
                .statusCode(404)
                .body("message", is("Product with id: " + productId + " not found"))
                .body("statusCode", is(404));
    }

    @Test
    void shouldGetProductByName() {
        Product product = productRepository.save(
                new Product("ProductName2", "Lorem ipsum dolor sit amet, consectetur adipiscing elit.", "ShortDescription", "BrandName", "image1.jpg", 100.0, 10, null, null, null)
        );

        // Given
        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/api/v1/products/name/ProductName2")
                .then()
                .statusCode(200)
                .body("product.id", is(product.getId().intValue()))
                .body("product.name", is(product.getName()))
                .body("product.description", is(product.getDescription()))
                .body("product.shortDescription", is(product.getShortDescription()))
                .body("product.brand", is(product.getBrand()))
                .body("product.image", is(product.getImage()))
                .body("product.unitPrice", is(product.getUnitPrice().floatValue()))
                .body("product.quantityPerUnit", is(product.getQuantityPerUnit()));
    }

    @Test
    void getProductByName_shouldReturn400_whenProductNameIsBlank() {
        // Given
        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/api/v1/products/name/{0}", " ")
                .then()
                .statusCode(400)
                .body("message", is("Validation error: getProductByName.name: must not be blank"))
                .body("statusCode", is(400));
    }

    @Test
    void getProductByName_shouldReturn404_whenProductNameNotFound() {
        String productName = "Lorem Ipsum";

        // Given
        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/api/v1/products/name/{0}", productName)
                .then()
                .statusCode(404)
                .body("message", is("Product with name: " + productName + " not found"))
                .body("statusCode", is(404));
    }
}