package org.example.mediashop.Controller;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.example.mediashop.Data.Entity.Category;
import org.example.mediashop.Data.Entity.User;
import org.example.mediashop.Repository.CategoryRepository;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

class CategoryControllerTest extends IntegrationTest {

    @LocalServerPort
    private Integer port;

    @Autowired
    private CategoryRepository categoryRepository;

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

    Category category;
    List<Category> subCategoryList;

    String token;

    @BeforeEach
    void setUp() throws JsonProcessingException {
        RestAssured.baseURI = "http://localhost:" + port;
        categoryRepository.deleteAll();
        userRepository.deleteAll();

        category = categoryRepository.save(new Category(null, "CategoryTitle", "CategoryDescription", "MetaTitle", "MetaDescription", "new, category", "new-category-1", null, null, null));

        subCategoryList = new ArrayList<>();
        subCategoryList.add(new Category(category.getId(), "CategoryTitle2", "CategoryDescription2", "MetaTitle2", "MetaDescription2", "new, category", "new-category-2", null, null, null));
        subCategoryList.add(new Category(category.getId(), "CategoryTitle3", "CategoryDescription3", "MetaTitle3", "MetaDescription3", "new, category", "new-category-3", null, null, null));

        categoryRepository.saveAll(subCategoryList);

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
    void shouldGetAllCategories() {
        // Given
        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/api/v1/categories")
                .then()
                .statusCode(200)
                .body("category.", hasSize(3));
    }

    @Test
    void getAllCategories_shouldReturn404_whenCategoryNotFound() {
        categoryRepository.deleteAll();
        // Given
        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/api/v1/categories")
                .then()
                .statusCode(404)
                .body("message", is("Categories not found"))
                .body("statusCode", is(404));
    }

    @Test
    void shouldGetCategoryById() {
        // Given
        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/api/v1/categories/id/{0}", category.getId().intValue())
                .then()
                .statusCode(200)
                .body("category.id", is(category.getId().intValue()))
                .body("category.title", is(category.getTitle()))
                .body("category.description", is(category.getDescription()))
                .body("category.metaTitle", is(category.getMetaTitle()))
                .body("category.metaDescription", is(category.getMetaDescription()))
                .body("category.metaKeywords", is(category.getMetaKeywords()))
                .body("category.slug", is(category.getSlug()));
    }

    @Test
    void getCategoryById_shouldReturn400_whenCategoryIdIsInvalid() {
        long categoryId = 0L;

        // Given
        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/api/v1/categories/id/{0}", categoryId)
                .then()
                .statusCode(400)
                .body("message", is("getCategoryById.id: must be greater than " + categoryId))
                .body("statusCode", is(400));
    }

    @Test
    void getCategoryById_shouldReturn404_whenCategoryIdNotFound() {
        long categoryId = 10L;

        // Given
        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/api/v1/categories/id/{0}", categoryId)
                .then()
                .statusCode(404)
                .body("message", is("Category with id: " + categoryId + " not found"))
                .body("statusCode", is(404));
    }

    @Test
    void shouldGetCategoryByTitle() {
        // Given
        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/api/v1/categories/title/CategoryTitle")
                .then()
                .statusCode(200)
                .body("category.id", is(category.getId().intValue()))
                .body("category.title", is(category.getTitle()))
                .body("category.description", is(category.getDescription()))
                .body("category.metaTitle", is(category.getMetaTitle()))
                .body("category.metaDescription", is(category.getMetaDescription()))
                .body("category.metaKeywords", is(category.getMetaKeywords()))
                .body("category.slug", is(category.getSlug()));
    }

    @Test
    void getCategoryByTitle_shouldReturn400_whenCategoryTitleIsBlank() {
        // Given
        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/api/v1/categories/title/{0}", " ")
                .then()
                .statusCode(400)
                .body("message", is("getCategoryByTitle.title: must not be blank"))
                .body("statusCode", is(400));
    }

    @Test
    void getCategoryByTitle_shouldReturn404_whenCategoryTitleNotFound() {
        String categoryName = "Lorem Ipsum";

        // Given
        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/api/v1/categories/title/{0}", categoryName)
                .then()
                .statusCode(404)
                .body("message", is("Category with title: " + categoryName + " not found"))
                .body("statusCode", is(404));
    }

    @Test
    void shouldGetCategoriesByParentId() {
        // Given
        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/api/v1/categories/parentId/{0}", category.getId().intValue())
                .then()
                .statusCode(200)
                .body("category.id", is(List.of(subCategoryList.get(0).getId().intValue(), subCategoryList.get(1).getId().intValue())))
                .body("category.title", is(List.of(subCategoryList.get(0).getTitle(), subCategoryList.get(1).getTitle())))
                .body("category.description", is(List.of(subCategoryList.get(0).getDescription(), subCategoryList.get(1).getDescription())))
                .body("category.metaTitle", is(List.of(subCategoryList.get(0).getMetaTitle(), subCategoryList.get(1).getMetaTitle())))
                .body("category.metaDescription", is(List.of(subCategoryList.get(0).getMetaDescription(), subCategoryList.get(1).getMetaDescription())))
                .body("category.metaKeywords", is(List.of(subCategoryList.get(0).getMetaKeywords(), subCategoryList.get(1).getMetaKeywords())))
                .body("category.slug", is(List.of(subCategoryList.get(0).getSlug(), subCategoryList.get(1).getSlug())));
    }

    @Test
    void getCategoriesByParentId_shouldReturn400_whenCategoryIdIsInvalid() {
        final Long parentId = 0L;
        // Given
        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/api/v1/categories/parentId/{0}", parentId)
                .then()
                .statusCode(400)
                .body("message", is("getCategoriesByParentId.parentId: must be greater than " + parentId))
                .body("statusCode", is(400));
    }

    @Test
    void getCategoriesByParentId_shouldReturn404_whenCategoryIdNotFound() {
        final Long parentId = 10L;

        // Given
        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/api/v1/categories/parentId/{0}", parentId)
                .then()
                .statusCode(404)
                .body("message", is("Category with parentId: " + parentId + " not found"))
                .body("statusCode", is(404));
    }

    @Test
    void shouldCreateCategory() throws JsonProcessingException {
        Map<String, Object> category = new HashMap<>();
        category.put("parentId", null);
        category.put("title", "categoryTitle");
        category.put("description", "categoryDescription");
        category.put("metaTitle", "categoryMetaTitle");
        category.put("metaDescription", "categoryMetaDescription");
        category.put("metaKeywords", "categoryMetaKeywords");
        category.put("slug", "category-slug");

        // Convert the Map to JSON string
        String jsonBody = new ObjectMapper().writeValueAsString(category);

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
                .post("/api/v1/categories/new")
                .then()
                .statusCode(201)
                .body("category.id", is(notNullValue()))
                .body("category.title", is("categoryTitle"))
                .body("category.description", is("categoryDescription"))
                .body("category.metaTitle", is("categoryMetaTitle"))
                .body("category.metaDescription", is("categoryMetaDescription"))
                .body("category.metaKeywords", is("categoryMetaKeywords"))
                .body("category.slug", is("category-slug"));
    }
}