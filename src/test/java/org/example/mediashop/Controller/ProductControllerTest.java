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
import org.testcontainers.shaded.com.fasterxml.jackson.core.JsonProcessingException;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

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

    List<Product> productList;

    @BeforeEach
    void setUp() {
        RestAssured.baseURI = "http://localhost:" + port;
        productRepository.deleteAll();

        productList = new ArrayList<>();
        productList.add(new Product("ProductName1", "Lorem ipsum dolor sit amet, consectetur adipiscing elit.", "ShortDescription", "BrandName", "image1.jpg", 100.0, 10, null, null, null));
        productList.add(new Product("ProductName2", "Lorem ipsum dolor sit amet, consectetur adipiscing elit.", "ShortDescription", "BrandName", "image1.jpg", 100.0, 10, null, null, null));
        productRepository.saveAll(productList);
    }

    @Test
    void shouldGetProductById() {
        // Given
        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/api/v1/products/id/{0}", productList.getFirst().getId().intValue())
                .then()
                .statusCode(200)
                .body("product.id", is(productList.getFirst().getId().intValue()))
                .body("product.name", is(productList.getFirst().getName()))
                .body("product.description", is(productList.getFirst().getDescription()))
                .body("product.shortDescription", is(productList.getFirst().getShortDescription()))
                .body("product.brand", is(productList.getFirst().getBrand()))
                .body("product.image", is(productList.getFirst().getImage()))
                .body("product.unitPrice", is(productList.getFirst().getUnitPrice().floatValue()))
                .body("product.quantityPerUnit", is(productList.getFirst().getQuantityPerUnit()));
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
                .body("message", is("getProductById.id: must be greater than " + productId))
                .body("statusCode", is(400));
    }

    @Test
    void getProductById_shouldReturn404_whenProductIdNotFound() {
        long productId = 100L;

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
        String productName = "ProductName2";
        // Given
        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/api/v1/products/name/{0}", productName)
                .then()
                .statusCode(200)
                .body("product.id", is(productList.get(1).getId().intValue()))
                .body("product.name", is(productList.get(1).getName()))
                .body("product.description", is(productList.get(1).getDescription()))
                .body("product.shortDescription", is(productList.get(1).getShortDescription()))
                .body("product.brand", is(productList.get(1).getBrand()))
                .body("product.image", is(productList.get(1).getImage()))
                .body("product.unitPrice", is(productList.get(1).getUnitPrice().floatValue()))
                .body("product.quantityPerUnit", is(productList.get(1).getQuantityPerUnit()));
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
                .body("message", is("getProductByName.name: must not be blank"))
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

    @Test
    void shouldCreateProduct() throws JsonProcessingException {
        Map<String, Object> product = new HashMap<>();
        product.put("name", "Mobile Diax Phone");
        product.put("description", "Diax description");
        product.put("shortDescription", "Diax shortDescription");
        product.put("brand", "Diax");
        product.put("image", "Diax.img.png");
        product.put("unitPrice", 1999.99);
        product.put("quantityPerUnit", 1);

        // Convert the Map to JSON string
        String jsonBody = new ObjectMapper().writeValueAsString(product);

        // Given
        given()
                .contentType(ContentType.JSON)
                .when()
                .body(jsonBody)
                .post("/api/v1/products/new")
                .then()
                .statusCode(201)
                .body("product.id", is(notNullValue()))
                .body("product.name", is("Mobile Diax Phone"))
                .body("product.description", is("Diax description"))
                .body("product.shortDescription", is("Diax shortDescription"))
                .body("product.brand", is("Diax"))
                .body("product.image", is("Diax.img.png"))
                .body("product.unitPrice", is( 1999.99F))
                .body("product.quantityPerUnit", is(1));
    }

}