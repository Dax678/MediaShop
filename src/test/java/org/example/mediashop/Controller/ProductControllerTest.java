package org.example.mediashop.Controller;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.example.mediashop.Data.Entity.*;
import org.example.mediashop.Repository.DiscountRepository;
import org.example.mediashop.Repository.ProductDiscountRepository;
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

import java.time.LocalDateTime;
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

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DiscountRepository discountRepository;

    @Autowired
    private ProductDiscountRepository productDiscountRepository;

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

    List<Product> productList;

    List<Discount> discountList;

    List<ProductDiscount> productDiscountList;

    String token;

    @BeforeEach
    void setUp() throws JsonProcessingException {
        RestAssured.baseURI = "http://localhost:" + port;
        productDiscountRepository.truncateAllData();
        productRepository.deleteAll();
        userRepository.deleteAll();

        productList = new ArrayList<>();
        productList.add(new Product("ProductName1", "Lorem ipsum dolor sit amet, consectetur adipiscing elit.", "ShortDescription", "BrandName", "image1.jpg", 100.0, 10, 5.0F, true));
        productList.add(new Product("ProductName2", "Lorem ipsum dolor sit amet, consectetur adipiscing elit.", "ShortDescription", "BrandName", "image1.jpg", 100.0, 10, 4.0F, true));
        productRepository.saveAll(productList);

        discountList = new ArrayList<>();
        discountList.add(new Discount(100.0, "CODE1", LocalDateTime.now().minusDays(1), LocalDateTime.now().plusDays(1), 1, 0.0));
        discountList.add(new Discount(50.0, "CODE2", LocalDateTime.now().minusDays(1), LocalDateTime.now().plusDays(1), 2, 100.0));
        discountRepository.saveAll(discountList);

        productDiscountList = new ArrayList<>();
        productDiscountList.add(new ProductDiscount(new ProductDiscountKey(productList.get(0).getId(), discountList.get(0).getId()), productList.get(0), discountList.get(0)));
        productDiscountList.add(new ProductDiscount(new ProductDiscountKey(productList.get(1).getId(), discountList.get(1).getId()), productList.get(1), discountList.get(1)));
        productDiscountRepository.saveAll(productDiscountList);

        userRepository.save(new User("userTest", passwordEncoder.encode("passwordTest"), "email@test.com", "ROLE_USER"));
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
    void shouldGetProductsByDiscount() {
        String discountCode = "CODE1";
        // Given
        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/api/v1/products/discountCode/{code}", discountCode)
                .then()
                .statusCode(200)
                .body("product[0].id", is(productList.get(0).getId().intValue()))
                .body("product[0].name", is(productList.get(0).getName()))
                .body("product[0].description", is(productList.get(0).getDescription()))
                .body("product[0].shortDescription", is(productList.get(0).getShortDescription()))
                .body("product[0].brand", is(productList.get(0).getBrand()))
                .body("product[0].image", is(productList.get(0).getImage()))
                .body("product[0].unitPrice", is(productList.get(0).getUnitPrice().floatValue()))
                .body("product[0].quantityPerUnit", is(productList.get(0).getQuantityPerUnit()))
                .body("discount.id", is(discountList.get(0).getId().intValue()))
                .body("discount.value", is(discountList.get(0).getValue().floatValue()))
                .body("discount.code", is(discountList.get(0).getCode()))
                .body("discount.maxUsage", is(discountList.get(0).getMaxUsage()))
                .body("discount.minPurchaseAmount", is(discountList.get(0).getMinPurchaseAmount().floatValue()));
    }

    @Test
    void getProductsByDiscount_shouldReturn400_whenDiscountCodeIsBlank() {
        // Given
        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/api/v1/products/discountCode/{code}", " ")
                .then()
                .statusCode(400)
                .body("message", is("getProductsByDiscount.discountCode: must not be blank"))
                .body("statusCode", is(400));
    }

    @Test
    void getProductsByDiscount_shouldReturn404_whenDiscountCodeNotFound() {
        String discountCode = "CODE3";

        // Given
        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/api/v1/products/discountCode/{code}", discountCode)
                .then()
                .statusCode(404)
                .body("message", is("Discount with code: " + discountCode + " not found"))
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
        product.put("rating", 5.0F);
        product.put("isAvailable", false);

        // Convert the Map to JSON string
        String jsonBody = new ObjectMapper().writeValueAsString(product);

        String adminToken = given()
                .contentType(ContentType.JSON)
                .when()
                .body(new ObjectMapper().writeValueAsString(Map.of("username", "adminTest", "password", "passwordTest")))
                .post("/api/v1/auth/login")
                .then()
                .extract().path("token");

        // Given
        given()
                .header("Authorization", "Bearer " + adminToken)
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
                .body("product.quantityPerUnit", is(1))
                .body("product.rating", is(5.0F))
                .body("product.isAvailable", is(false));
    }
}