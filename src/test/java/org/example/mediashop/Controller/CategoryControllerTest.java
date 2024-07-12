package org.example.mediashop.Controller;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.example.mediashop.Data.Entity.Category;
import org.example.mediashop.Repository.CategoryRepository;
import org.example.mediashop.TestConfig.IntegrationTest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

class CategoryControllerTest extends IntegrationTest {

    @LocalServerPort
    private Integer port;

    @Autowired
    private CategoryRepository categoryRepository;

    @BeforeAll
    static void beforeAll() {
        postgres.start();
    }

    @AfterAll
    static void afterAll() {
        postgres.stop();
    }

    List<Category> categoryList;

    @BeforeEach
    void setUp() {
        RestAssured.baseURI = "http://localhost:" + port;
        categoryRepository.deleteAll();

        categoryList = new ArrayList<>();
        categoryList.add(new Category(null, "CategoryTitle", "CategoryDescription", "MetaTitle", "MetaDescription", "new, category", "new-category-1", null, null, null));
        categoryList.add(new Category(null, "CategoryTitle2", "CategoryDescription2", "MetaTitle2", "MetaDescription2", "new, category", "new-category-2", null, null, null));
        categoryList.add(new Category(null, "CategoryTitle3", "CategoryDescription3", "MetaTitle3", "MetaDescription3", "new, category", "new-category-3", null, null, null));

        categoryRepository.saveAll(categoryList);
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
                .get("/api/v1/categories/id/{0}", categoryList.getFirst().getId().intValue())
                .then()
                .statusCode(200)
                .body("category.id", is(categoryList.getFirst().getId().intValue()))
                .body("category.title", is(categoryList.getFirst().getTitle()))
                .body("category.description", is(categoryList.getFirst().getDescription()))
                .body("category.metaTitle", is(categoryList.getFirst().getMetaTitle()))
                .body("category.metaDescription", is(categoryList.getFirst().getMetaDescription()))
                .body("category.metaKeywords", is(categoryList.getFirst().getMetaKeywords()))
                .body("category.slug", is(categoryList.getFirst().getSlug()));
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
                .body("category.id", is(categoryList.getFirst().getId().intValue()))
                .body("category.title", is(categoryList.getFirst().getTitle()))
                .body("category.description", is(categoryList.getFirst().getDescription()))
                .body("category.metaTitle", is(categoryList.getFirst().getMetaTitle()))
                .body("category.metaDescription", is(categoryList.getFirst().getMetaDescription()))
                .body("category.metaKeywords", is(categoryList.getFirst().getMetaKeywords()))
                .body("category.slug", is(categoryList.getFirst().getSlug()));
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
}