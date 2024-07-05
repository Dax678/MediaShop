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

    @BeforeEach
    void setUp() {
        RestAssured.baseURI = "http://localhost:" + port;
        categoryRepository.deleteAll();
    }

    @Test
    void shouldGetAllCategories() {
        categoryRepository.saveAll(List.of(
                new Category(null, "CategoryTitle", "CategoryDescription", "MetaTitle", "MetaDescription", "new, category", "new-category-1", null, null, null),
                new Category(null, "CategoryTitle2", "CategoryDescription2", "MetaTitle2", "MetaDescription2", "new, category", "new-category-2", null, null, null),
                new Category(null, "CategoryTitle3", "CategoryDescription3", "MetaTitle3", "MetaDescription3", "new, category", "new-category-3", null, null, null)
        ));

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
    void shouldReturn404WhenCategoryNotFound() {
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
        Category category = categoryRepository.save(
                new Category(null, "CategoryTitle", "CategoryDescription", "MetaTitle", "MetaDescription", "new, category", "new-category-1", null, null, null)
        );

        // Given
        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/api/v1/categories/id/{0}", category.getId().intValue())
                .then()
                .statusCode(200)
                .body("category.id", is(category.getId().intValue()))
                .body("category.title", is("CategoryTitle"))
                .body("category.description", is("CategoryDescription"))
                .body("category.metaTitle", is("MetaTitle"))
                .body("category.metaDescription", is("MetaDescription"))
                .body("category.metaKeywords", is("new, category"))
                .body("category.slug", is("new-category-1"));
    }

    @Test
    void shouldReturn400WhenCategoryIdIsInvalid() {
        long categoryId = 0L;

        // Given
        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/api/v1/categories/id/{0}", categoryId)
                .then()
                .statusCode(400)
                .body("message", is("Validation error: getCategoryById.id: must be greater than " + categoryId))
                .body("statusCode", is(400));
    }

    @Test
    void shouldReturn404WhenCategoryIdNotFound() {
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
        Category category = categoryRepository.save(
                new Category(null, "CategoryTitle", "CategoryDescription", "MetaTitle", "MetaDescription", "new, category", "new-category-1", null, null, null)
        );

        // Given
        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/api/v1/categories/title/CategoryTitle")
                .then()
                .statusCode(200)
                .body("category.id", is(category.getId().intValue()))
                .body("category.title", is("CategoryTitle"))
                .body("category.description", is("CategoryDescription"))
                .body("category.metaTitle", is("MetaTitle"))
                .body("category.metaDescription", is("MetaDescription"))
                .body("category.metaKeywords", is("new, category"))
                .body("category.slug", is("new-category-1"));
    }

    @Test
    void shouldReturn400WhenCategoryTitleIsBlank() {
        // Given
        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/api/v1/categories/title/{0}", " ")
                .then()
                .statusCode(400)
                .body("message", is("Validation error: getCategoryByTitle.title: must not be blank"))
                .body("statusCode", is(400));
    }

    @Test
    void shouldReturn404WhenCategoryTitleNotFound() {
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