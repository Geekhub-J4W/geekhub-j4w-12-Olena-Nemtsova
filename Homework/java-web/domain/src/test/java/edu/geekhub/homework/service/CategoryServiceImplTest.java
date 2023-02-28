package edu.geekhub.homework.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import edu.geekhub.homework.domain.Category;
import edu.geekhub.homework.domain.CategoryValidator;
import edu.geekhub.homework.repository.interfaces.CategoryRepository;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessException;

@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {
    private CategoryServiceImpl categoryService;
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private CategoryValidator categoryValidator;

    @BeforeEach
    void setUp() {
        categoryService = new CategoryServiceImpl(categoryRepository, categoryValidator);
    }

    @Test
    void can_get_categories() {
        List<Category> expectedCategories = List.of(new Category("Dairy"));
        when(categoryRepository.getCategories()).thenReturn(expectedCategories);

        List<Category> categories = categoryService.getCategories();

        assertEquals(expectedCategories, categories);
    }

    @Test
    void can_get_category_by_id() {
        Category dairy = new Category(1, "Dairy");
        when(categoryRepository.getCategories()).thenReturn(List.of(dairy));

        Category category = categoryService.getCategoryById(1);

        assertEquals(dairy, category);
    }

    @Test
    void can_get_null_category_by_wrong_id() {
        Category dairy = new Category(1, "Dairy");
        when(categoryRepository.getCategories()).thenReturn(List.of(dairy));

        Category category = categoryService.getCategoryById(0);

        assertNull(category);
    }

    @Test
    void can_add_category() {
        Category dairy = new Category(1, "Dairy");
        doNothing().when(categoryValidator).validate(any());
        doReturn(1).when(categoryRepository).addCategory(any());

        boolean successfulAdded = categoryService.addCategory(dairy);

        assertTrue(successfulAdded);
    }

    @Test
    void can_not_add_not_valid_category() {
        doThrow(new IllegalArgumentException()).when(categoryValidator).validate(any());

        boolean successfulAdded = categoryService.addCategory(null);

        assertFalse(successfulAdded);
    }

    @Test
    void can_not_add_category_not_added_to_repository() {
        doNothing().when(categoryValidator).validate(any());
        doReturn(-1).when(categoryRepository).addCategory(any());

        boolean successfulAdded = categoryService.addCategory(null);

        assertFalse(successfulAdded);
    }

    @Test
    void can_delete_category_by_id() {
        Category dairy = new Category(1, "Dairy");
        categoryService = spy(this.categoryService);
        doReturn(dairy).when(categoryService).getCategoryById(anyInt());
        doNothing().when(categoryRepository).deleteCategoryById(anyInt());

        boolean successfulDeleted = categoryService.deleteCategoryById(1);

        assertTrue(successfulDeleted);
    }

    @Test
    void can_not_delete_product_by_not_existing_id() {
        categoryService = spy(this.categoryService);
        doReturn(null).when(categoryService).getCategoryById(anyInt());

        boolean successfulDeleted = categoryService.deleteCategoryById(1);

        assertFalse(successfulDeleted);
    }

    @Test
    void can_not_delete_category_not_deleted_at_repository() {
        Category dairy = new Category(1, "Dairy");
        categoryService = spy(this.categoryService);
        doReturn(dairy).when(categoryService).getCategoryById(anyInt());
        doThrow(new DataAccessException("") {})
            .when(categoryRepository).deleteCategoryById(anyInt());

        boolean successfulDeleted = categoryService.deleteCategoryById(1);

        assertFalse(successfulDeleted);
    }

    @Test
    void can_update_category_by_id() {
        categoryService = spy(this.categoryService);
        doNothing().when(categoryValidator).validate(any());
        doReturn(new Category("Dairy")).when(categoryService).getCategoryById(anyInt());
        doNothing().when(categoryRepository).updateCategoryById(any(), anyInt());

        boolean successfulUpdated = categoryService.updateCategoryById(null, 1);

        assertTrue(successfulUpdated);
    }

    @Test
    void can_not_update_category_by_id_to_not_valid_category() {
        doThrow(new IllegalArgumentException()).when(categoryValidator).validate(any());

        boolean successfulUpdated = categoryService.updateCategoryById(null, 1);

        assertFalse(successfulUpdated);
    }

    @Test
    void can_not_update_category_by_not_existing_id() {
        categoryService = spy(this.categoryService);
        doNothing().when(categoryValidator).validate(any());
        doReturn(null).when(categoryService).getCategoryById(anyInt());

        boolean successfulUpdated = categoryService.updateCategoryById(null, 1);

        assertFalse(successfulUpdated);
    }

    @Test
    void can_not_update_product_not_updated_at_repository() {
        categoryService = spy(this.categoryService);
        doNothing().when(categoryValidator).validate(any());
        doReturn(new Category("Dairy")).when(categoryService).getCategoryById(anyInt());
        doThrow(new DataAccessException("") {
        }).when(categoryRepository).updateCategoryById(any(), anyInt());

        boolean successfulUpdated = categoryService.updateCategoryById(null, 1);

        assertFalse(successfulUpdated);
    }
}
