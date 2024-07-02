package org.example.mediashop.Controller;

import org.example.mediashop.Data.Entity.Category;
import org.example.mediashop.Service.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(SpringExtension.class)
@WebMvcTest(CategoryController.class)
class CategoryControllerTest {

    @MockBean
    private CategoryService categoryService;

    private CategoryController categoryController;

    @Captor
    private ArgumentCaptor<Long> idCaptor;

    List<Category> expectedCategory = new ArrayList<>();

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        categoryController = new CategoryController(categoryService);

        expectedCategory = Arrays.asList(
                new Category(1L, null, null, "New Category 1", "Description for new category 1", "Meta title 1", "Meta description 1", "new, category", "new-category-1", null),
                new Category(2L, null, null, "New Category 2", "Description for new category 2", "Meta title 2", "Meta description 2", "new, category", "new-category-2", null),
                new Category(3L, null, null, "New Category 3", "Description for new category 3", "Meta title 3", "Meta description 3", "new, category", "new-category-3", null)
        );
    }

    @Test
    void testGetAllCategories_returnsCategoriesList() {
        // When
        when(categoryService.getAllCategories()).thenReturn(expectedCategory);
        ResponseEntity<Map<String, List<Category>>> responseEntity = categoryController.getAllCategories();

        // Then
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        List<Category> result = responseEntity.getBody().get("category");

        assertEquals(expectedCategory, result);
    }

    @Test
    public void testGetCategoryById_whenCategoryExists_returnsCategory() {
        // Given
        Long categoryId = 1L;
        when(categoryService.getCategoryById(anyLong())).thenReturn(expectedCategory.getFirst());

        // When
        ResponseEntity<Map<String, Category>> responseEntity = categoryController.getCategoryById(categoryId);

        // Then
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        Map<String, Category> responseBody = responseEntity.getBody();

        assertNotNull(responseBody);
        assertEquals(expectedCategory.getFirst(), responseBody.get("category"));

        verify(categoryService).getCategoryById(idCaptor.capture());
        assertEquals(categoryId, idCaptor.getValue());
    }

    @Test
    void testGetCategoryByTitle_whenCategoryExists_returnsCategory() {
        // Given
        String categoryTitle = "New Category 2";
        when(categoryService.getCategoryByTitle(anyString())).thenReturn(expectedCategory.getFirst());

        // When
        ResponseEntity<Map<String, Category>> responseEntity = categoryController.getCategoryByTitle(categoryTitle);

        // Then
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        Map<String, Category> responseBody = responseEntity.getBody();

        assertNotNull(responseBody);
        assertEquals(expectedCategory.getFirst(), responseBody.get("category"));
    }
}