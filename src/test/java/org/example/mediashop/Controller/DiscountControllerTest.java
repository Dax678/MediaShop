package org.example.mediashop.Controller;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.example.mediashop.Data.Entity.Discount;
import org.example.mediashop.Data.Entity.User;
import org.example.mediashop.Repository.DiscountRepository;
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

public class DiscountControllerTest extends IntegrationTest  {

    @LocalServerPort
    private Integer port;

    @Autowired
    private DiscountRepository discountRepository;

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

    List<Discount> discountList;

    String token;

    @BeforeEach
    void setUp() throws JsonProcessingException {
        RestAssured.baseURI = "http://localhost:" + port;
        discountRepository.deleteAll();
        userRepository.deleteAll();

        discountList = new ArrayList<>();
        discountList.add(discountRepository.save(new Discount(100.0, "DiscountCode1",  LocalDateTime.parse("2024-07-10T00:00:00"),  LocalDateTime.parse("2024-07-15T00:00:00"), 1, 0.0)));
        discountList.add(discountRepository.save(new Discount(100.0, "DiscountCode2",  LocalDateTime.parse("2024-07-10T00:00:00"),  LocalDateTime.parse("2024-07-15T00:00:00"), 1, 0.0)));
        discountRepository.saveAll(discountList);

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
    void shouldGetDiscountById() {
        // Given
        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/api/v1/discounts/id/{0}", discountList.getFirst().getId().intValue())
                .then()
                .statusCode(200)
                .body("discount.id", is(discountList.getFirst().getId().intValue()))
                .body("discount.value", is(discountList.getFirst().getValue().floatValue()))
                .body("discount.code", is(discountList.getFirst().getCode()))
                .body("discount.startDate", is("2024-07-10T00:00:00"))
                .body("discount.endDate", is("2024-07-15T00:00:00"))
                .body("discount.maxUsage", is(discountList.getFirst().getMaxUsage()))
                .body("discount.minPurchaseAmount", is(discountList.getFirst().getMinPurchaseAmount().floatValue()));
    }

    @Test
    void getDiscountById_shouldReturn400_whenDiscountIdIsInvalid() {
        long discountId = 0L;

        // Given
        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/api/v1/discounts/id/{0}", discountId)
                .then()
                .statusCode(400)
                .body("message", is("getDiscountById.id: must be greater than " + discountId))
                .body("statusCode", is(400));
    }

    @Test
    void getDiscountById_shouldReturn404_whenDiscountIdNotFound() {
        long discountId = 100L;

        // Given
        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/api/v1/discounts/id/{0}", discountId)
                .then()
                .statusCode(404)
                .body("message", is("Discount with id: " + discountId + " not found"))
                .body("statusCode", is(404));
    }

    @Test
    void shouldCreateDiscount() throws JsonProcessingException {
        Map<String, Object> discount = new HashMap<>();
        discount.put("value", 10);
        discount.put("code", "SUMMER_SALE");
        discount.put("startDate", "2023-06-01T00:00:00");
        discount.put("endDate", "2023-06-05T00:00:00");
        discount.put("maxUsage", 1);
        discount.put("minPurchaseAmount", 1999.99);

        // Convert the Map to JSON string
        String jsonBody = new ObjectMapper().writeValueAsString(discount);

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
                .post("/api/v1/discounts/new")
                .then()
                .statusCode(201)
                .body("discount.id", is(notNullValue()))
                .body("discount.value", is(10.0F))
                .body("discount.code", is("SUMMER_SALE"))
                .body("discount.startDate", is("2023-06-01T00:00:00"))
                .body("discount.endDate", is("2023-06-05T00:00:00"))
                .body("discount.maxUsage", is(1))
                .body("discount.minPurchaseAmount", is(1999.99F));
    }
}
