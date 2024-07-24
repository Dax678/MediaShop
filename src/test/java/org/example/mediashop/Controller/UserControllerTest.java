package org.example.mediashop.Controller;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.example.mediashop.Data.Entity.User;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserControllerTest extends IntegrationTest {

    @LocalServerPort
    private Integer port;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private User user;
    private User admin;

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
        userRepository.deleteAll();

        user = userRepository.save(new User("userTest", passwordEncoder.encode("passwordTest"), "email@test.com", "ROLE_USER"));
        userRepository.save(new User("adminTest", passwordEncoder.encode("passwordTest"), "email.admin@test.com", "ROLE_ADMIN"));

    }

    @Test
    void shouldRegisterNewUser() throws JsonProcessingException {
        Map<String, Object> user = new HashMap<>();
        user.put("username", "userRegisterTest");
        user.put("password", "passwordTest");
        user.put("email", "emailRegister@test.com");
        user.put("firstName", "firstNameTest");
        user.put("lastName", "lastNameTest");

        // Convert the Map to JSON string
        String jsonBody = new ObjectMapper().writeValueAsString(user);

        // Given
        given()
                .contentType(ContentType.JSON)
                .when()
                .body(jsonBody)
                .post("/api/v1/auth/signup")
                .then()
                .statusCode(200)
                .body("id", is(notNullValue()))
                .body("username", is("userRegisterTest"))
                .body("role", is("ROLE_USER"));

        User registeredUser = userRepository.findByUsername("userTest").orElseThrow(() -> new RuntimeException("User not found"));

        // Verify the password
        assertTrue(passwordEncoder.matches("passwordTest", registeredUser.getPassword()), "The password does not match!");

    }

    @Test
    void registerNewUser_shouldReturn401_WhenUsernameIsTaken() throws JsonProcessingException {
        Map<String, Object> user = new HashMap<>();
        user.put("username", "userTest");
        user.put("password", "passwordTest");
        user.put("email", "emailTest1@test.com");
        user.put("firstName", "firstNameTest");
        user.put("lastName", "lastNameTest");

        // Convert the Map to JSON string
        String jsonBody = new ObjectMapper().writeValueAsString(user);

        // Given
        given()
                .contentType(ContentType.JSON)
                .when()
                .body(jsonBody)
                .post("/api/v1/auth/signup")
                .then()
                .statusCode(401)
                .body("path", is("/error"))
                .body("error", is("Unauthorized"))
                .body("message", is("Full authentication is required to access this resource"))
                .body("status", is(401));
    }

    @Test
    void registerNewUser_shouldReturn401_WhenEmailIsTaken() throws JsonProcessingException {
        Map<String, Object> user = new HashMap<>();
        user.put("username", "userTest1");
        user.put("password", "passwordTest");
        user.put("email", "email@test.com");
        user.put("firstName", "firstNameTest");
        user.put("lastName", "lastNameTest");

        // Convert the Map to JSON string
        String jsonBody = new ObjectMapper().writeValueAsString(user);

        // Given
        given()
                .contentType(ContentType.JSON)
                .when()
                .body(jsonBody)
                .post("/api/v1/auth/signup")
                .then()
                .statusCode(401)
                .body("path", is("/error"))
                .body("error", is("Unauthorized"))
                .body("message", is("Full authentication is required to access this resource"))
                .body("status", is(401));
    }

    @Test
    void shouldAuthenticate() throws JsonProcessingException {
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("username", "userTest");
        userMap.put("password", "passwordTest");

        // Convert the Map to JSON string
        String jsonBody = new ObjectMapper().writeValueAsString(userMap);

        // Given
        given()
                .contentType(ContentType.JSON)
                .when()
                .body(jsonBody)
                .post("/api/v1/auth/login")
                .then()
                .statusCode(200)
                .body("id", is(user.getId().intValue()))
                .body("username", is(user.getUsername()))
                .body("email", is(user.getEmail()))
                .body("type", is("Bearer"))
                .body("token", is(notNullValue()))
                .body("roles", is(List.of(user.getRole())));
    }

    @Test
    void authenticate_shouldReturn400_WhenUsernameIsBlank() throws JsonProcessingException {
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("username", "");
        userMap.put("password", "passwordTest");

        // Convert the Map to JSON string
        String jsonBody = new ObjectMapper().writeValueAsString(userMap);

        // Given
        given()
                .contentType(ContentType.JSON)
                .when()
                .body(jsonBody)
                .post("/api/v1/auth/login")
                .then()
                .statusCode(400)
                .body("statusCode", is(400))
                .body("message", is("Username cannot be blank"));
    }

    @Test
    void authenticate_shouldReturn400_WhenPasswordIsBlank() throws JsonProcessingException {
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("username", "userTest");
        userMap.put("password", "");

        // Convert the Map to JSON string
        String jsonBody = new ObjectMapper().writeValueAsString(userMap);

        // Given
        given()
                .contentType(ContentType.JSON)
                .when()
                .body(jsonBody)
                .post("/api/v1/auth/login")
                .then()
                .statusCode(400)
                .body("statusCode", is(400))
                .body("message", is("Password cannot be blank"));
    }

    @Test
    void authenticate_shouldReturn401_WhenUsernameIsInvalid() throws JsonProcessingException {
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("username", "userTest1");
        userMap.put("password", "passwordTest");

        // Convert the Map to JSON string
        String jsonBody = new ObjectMapper().writeValueAsString(userMap);

        // Given
        given()
                .contentType(ContentType.JSON)
                .when()
                .body(jsonBody)
                .post("/api/v1/auth/login")
                .then()
                .statusCode(401)
                .body("path", is("/api/v1/auth/login"))
                .body("error", is("Unauthorized"))
                .body("message", is("Bad credentials"))
                .body("status", is(401));
    }

    @Test
    void authenticate_shouldReturn401_WhenPasswordIsIncorrect() throws JsonProcessingException {
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("username", "userTest");
        userMap.put("password", "passwordTest1");

        // Convert the Map to JSON string
        String jsonBody = new ObjectMapper().writeValueAsString(userMap);

        // Given
        given()
                .contentType(ContentType.JSON)
                .when()
                .body(jsonBody)
                .post("/api/v1/auth/login")
                .then()
                .statusCode(401)
                .body("path", is("/api/v1/auth/login"))
                .body("error", is("Unauthorized"))
                .body("message", is("Bad credentials"))
                .body("status", is(401));
    }

    @Test
    void authenticate_ShouldReturn200_WhenGuestHasValidRole() {
        // Given
        given()
                .contentType(ContentType.TEXT)
                .when()
                .get("/api/v1/auth/roleTest/guest")
                .then()
                .statusCode(200)
                .body(is("Guest Test Successfully"));
    }

    @Test
    void authenticate_ShouldReturn200_WhenUserHasValidRole() throws JsonProcessingException {
        String userToken = given()
                .contentType(ContentType.JSON)
                .when()
                .body(new ObjectMapper().writeValueAsString(Map.of("username", "userTest", "password", "passwordTest")))
                .post("/api/v1/auth/login")
                .then()
                .extract().path("token");

        // Given
        given()
                .header("Authorization", "Bearer " + userToken)
                .contentType(ContentType.TEXT)
                .when()
                .get("/api/v1/auth/roleTest/user")
                .then()
                .statusCode(200)
                .body(is("User Role Test Successful"));
    }

    @Test
    void authenticate_ShouldReturn200_WhenAdminHasValidRole() throws JsonProcessingException {
        String userToken = given()
                .contentType(ContentType.JSON)
                .when()
                .body(new ObjectMapper().writeValueAsString(Map.of("username", "adminTest", "password", "passwordTest")))
                .post("/api/v1/auth/login")
                .then()
                .extract().path("token");

        // Given
        given()
                .header("Authorization", "Bearer " + userToken)
                .contentType(ContentType.TEXT)
                .when()
                .get("/api/v1/auth/roleTest/admin")
                .then()
                .statusCode(200)
                .body(is("Admin Role Test Successful"));
    }
}