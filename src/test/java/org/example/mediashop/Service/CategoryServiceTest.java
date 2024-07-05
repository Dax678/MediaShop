package org.example.mediashop.Service;

import org.example.mediashop.Configuration.Exception.NotFoundException;
import org.example.mediashop.Data.Entity.Category;
import org.example.mediashop.Repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    List<Category> expectedCategory = new ArrayList<>();

    static final String CATEGORY_TITLE = "New Category 1";
    static final String NONEXISTENT_CATEGORY_TITLE = "Nonexistent Category title";
    static final long CATEGORY_ID = 1L;
    static final String CATEGORY_EXCEPTION_MESSAGE = "Categories not found";
    static final String CATEGORY_ID_EXCEPTION_MESSAGE = "Category with id: " + CATEGORY_ID + " not found";
    static final String CATEGORY_TITLE_EXCEPTION_MESSAGE = "Category with title: " + NONEXISTENT_CATEGORY_TITLE + " not found";

    @BeforeEach
    public void setUp() {
        // Reset the mocks before each test
        Mockito.reset(categoryRepository);

        expectedCategory = Arrays.asList(
                new Category(1L, null, "New Category 1", "Description for new category 1", "Meta title 1", "Meta description 1", "new, category", "new-category-1", null, null, null),
                new Category(2L, null, "New Category 2", "Description for new category 2", "Meta title 2", "Meta description 2", "new, category", "new-category-2", null, null, null),
                new Category(3L, null, "New Category 3", "Description for new category 3", "Meta title 3", "Meta description 3", "new, category", "new-category-3", null, null, null)
        );
    }

    @Test
    public void testGetAllCategories_whenNonEmptyList_shouldReturnCategories() {
        // Given
        when(categoryRepository.findAll()).thenReturn(expectedCategory);

        // When
        List<Category> categories = categoryService.getAllCategories();

        // Then
        assert (categories != null && !categories.isEmpty());
        assertDoesNotThrow(() -> categoryService.getAllCategories());
    }

    @Test
    public void testGetAllCategories_whenEmptyList_shouldThrowCategoryNotFoundException() {
        // Given
        List<Category> emptyCategoryList = Collections.emptyList();
        when(categoryRepository.findAll()).thenReturn(emptyCategoryList);

        // When
        NotFoundException exception = assertThrows(NotFoundException.class, () -> categoryService.getAllCategories());
        String actualMessage = exception.getMessage();

        // Then
        assert (actualMessage != null && actualMessage.contains(CATEGORY_EXCEPTION_MESSAGE));
    }

    @Test
    public void testGetCategoryById_WhenCategoryExists_ShouldReturnCategory() {
        // Given
        when(categoryRepository.findCategoryById(anyLong())).thenReturn(Optional.ofNullable(expectedCategory.get(0)));

        // When
        Category result = categoryService.getCategoryById(CATEGORY_ID);

        // Then
        assertNotNull(result);
        assertEquals(expectedCategory.get(0), result);
    }

    @Test
    public void testGetCategoryById_WhenCategoryDoesNotExist_ShouldThrowCategoryNotFoundException() {
        // Given
        when(categoryRepository.findCategoryById(anyLong())).thenReturn(Optional.empty());

        // When
        NotFoundException exception = assertThrows(NotFoundException.class, () -> categoryService.getCategoryById(CATEGORY_ID));

        // Then
        assertEquals(exception.getMessage(), CATEGORY_ID_EXCEPTION_MESSAGE);
    }

    @Test
    public void testGetCategoryByTitle_WhenCategoryExists_ShouldReturnCategory() {
        // Given
        when(categoryRepository.findCategoryByTitle(anyString())).thenReturn(Optional.ofNullable(expectedCategory.get(0)));

        // When
        Category result = categoryService.getCategoryByTitle(CATEGORY_TITLE);

        // Then
        assertNotNull(result);
        assertEquals(expectedCategory.get(0), result);
    }

    @Test
    public void testGetCategoryByTitle_WhenCategoryDoesNotExist_ShouldThrowCategoryNotFoundException() {
        // Given
        when(categoryRepository.findCategoryByTitle(anyString())).thenReturn(Optional.empty());

        // When
        NotFoundException exception = assertThrows(NotFoundException.class, () -> categoryService.getCategoryByTitle(NONEXISTENT_CATEGORY_TITLE));

        // Then
        assertEquals(exception.getMessage(), CATEGORY_TITLE_EXCEPTION_MESSAGE);
    }
}