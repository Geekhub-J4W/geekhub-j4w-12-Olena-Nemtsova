package edu.geekhub.homework.categories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import edu.geekhub.homework.categories.interfaces.CategoryRepository;
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
    private Category category;

    @BeforeEach
    void setUp() {
        categoryService = new CategoryServiceImpl(categoryRepository, categoryValidator);
        category = new Category(1, "Dairy");
    }

    @Test
    void can_get_categories() {
        List<Category> expectedCategories = List.of(category);
        when(categoryRepository.getCategories()).thenReturn(expectedCategories);

        List<Category> categories = categoryService.getCategories();

        assertEquals(expectedCategories, categories);
    }

    @Test
    void can_get_category_by_id() {
        when(categoryRepository.getCategoryById(anyInt())).thenReturn(category);

        Category gotCategory = categoryService.getCategoryById(1);

        assertEquals(category, gotCategory);
    }

    @Test
    void can_get_null_category_by_wrong_id() {
        doReturn(null).when(categoryRepository).getCategoryById(anyInt());

        Category category = categoryService.getCategoryById(0);

        assertNull(category);
    }

    @Test
    void can_add_category() {
        doNothing().when(categoryValidator).validate(any());
        doReturn(1).when(categoryRepository).addCategory(any());
        doReturn(category).when(categoryRepository).getCategoryById(anyInt());

        Category addedCategory = categoryService.addCategory(category);

        assertNotNull(addedCategory);
    }

    @Test
    void can_not_add_not_valid_category() {
        doThrow(new IllegalArgumentException()).when(categoryValidator).validate(any());

        Category addedCategory = categoryService.addCategory(null);

        assertNull(addedCategory);
    }

    @Test
    void can_not_add_category_not_get_id_from_repository() {
        doNothing().when(categoryValidator).validate(any());
        doReturn(-1).when(categoryRepository).addCategory(any());

        Category addedCategory = categoryService.addCategory(category);

        assertNull(addedCategory);
    }

    @Test
    void can_not_add_category_not_added_at_repository() {
        doNothing().when(categoryValidator).validate(any());
        doThrow(new DataAccessException("") {
        }).when(categoryRepository).addCategory(any());

        Category addedCategory = categoryService.addCategory(category);

        assertNull(addedCategory);
    }

    @Test
    void can_delete_category_by_id() {
        doReturn(category).when(categoryRepository).getCategoryById(anyInt());
        doNothing().when(categoryRepository).deleteCategoryById(anyInt());

        boolean successfulDeleted = categoryService.deleteCategoryById(1);

        assertTrue(successfulDeleted);
    }

    @Test
    void can_not_delete_category_by_not_existing_id() {
        doReturn(null).when(categoryRepository).getCategoryById(anyInt());

        boolean successfulDeleted = categoryService.deleteCategoryById(1);

        assertFalse(successfulDeleted);
    }

    @Test
    void can_not_delete_category_not_deleted_at_repository() {
        doReturn(category).when(categoryRepository).getCategoryById(anyInt());
        doThrow(new DataAccessException("") {
        })
            .when(categoryRepository).deleteCategoryById(anyInt());

        boolean successfulDeleted = categoryService.deleteCategoryById(1);

        assertFalse(successfulDeleted);
    }

    @Test
    void can_update_category_by_id() {
        doNothing().when(categoryValidator).validate(any());
        doReturn(category).when(categoryRepository).getCategoryById(anyInt());
        doNothing().when(categoryRepository).updateCategoryById(any(), anyInt());

        Category updatedCategory = categoryService.updateCategoryById(category, 1);

        assertNotNull(updatedCategory);
    }

    @Test
    void can_not_update_category_by_id_to_not_valid_category() {
        doThrow(new IllegalArgumentException()).when(categoryValidator).validate(any());

        Category category = categoryService.updateCategoryById(null, 1);

        assertNull(category);
    }

    @Test
    void can_not_update_category_by_not_existing_id() {
        doNothing().when(categoryValidator).validate(any());
        doReturn(null).when(categoryRepository).getCategoryById(anyInt());

        Category category = categoryService.updateCategoryById(null, 1);

        assertNull(category);
    }

    @Test
    void can_not_update_category_not_updated_at_repository() {
        doNothing().when(categoryValidator).validate(any());
        doReturn(category).when(categoryRepository).getCategoryById(anyInt());
        doThrow(new DataAccessException("") {
        }).when(categoryRepository).updateCategoryById(any(), anyInt());

        Category updatedCategory = categoryService.updateCategoryById(category, 1);

        assertNull(updatedCategory);
    }
}
