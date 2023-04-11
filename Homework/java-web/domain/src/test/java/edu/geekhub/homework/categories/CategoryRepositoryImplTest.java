package edu.geekhub.homework.categories;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

@ExtendWith(MockitoExtension.class)
class CategoryRepositoryImplTest {
    private CategoryRepositoryImpl categoryRepository;
    @Mock
    private CategoryValidator categoryValidator;
    @Mock
    private NamedParameterJdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        categoryRepository = new CategoryRepositoryImpl(jdbcTemplate, categoryValidator);
    }

    @Test
    void can_not_add_not_valid_category() {
        doThrow(new IllegalArgumentException()).when(categoryValidator).validate(any());

        assertThrows(
            IllegalArgumentException.class,
            () -> categoryRepository.addCategory(null)
        );
    }

    @Test
    void can_not_add_not_added_at_database() {
        Category category = new Category("Dairy");
        doNothing().when(categoryValidator).validate(any());
        doThrow(new DataAccessException("") {
        }).when(jdbcTemplate).update(anyString(), any(), any());

        assertThrows(
            DataAccessException.class,
            () -> categoryRepository.addCategory(category)
        );
    }

    @Test
    void can_add_category() {
        doNothing().when(categoryValidator).validate(any());
        doReturn(1).when(jdbcTemplate).update(anyString(), any(), any());

        assertDoesNotThrow(
            () -> categoryRepository.addCategory(new Category("Dairy"))
        );
    }

    @Test
    void can_not_update_to_valid_category() {
        doThrow(new IllegalArgumentException()).when(categoryValidator).validate(any());

        assertThrows(
            IllegalArgumentException.class,
            () -> categoryRepository.updateCategoryById(null, 1)
        );
    }

    @Test
    void can_not_update_to_not_updated_category_at_database() {
        Category category = new Category("Dairy");
        doNothing().when(categoryValidator).validate(any());
        doThrow(new DataAccessException("") {
        }).when(jdbcTemplate).update(anyString(), (SqlParameterSource) any());

        assertThrows(
            DataAccessException.class,
            () -> categoryRepository.updateCategoryById(category, 1)
        );
    }

    @Test
    void can_update_category() {
        doNothing().when(categoryValidator).validate(any());
        doReturn(1).when(jdbcTemplate).update(anyString(), (SqlParameterSource) any());

        assertDoesNotThrow(
            () -> categoryRepository.updateCategoryById(new Category("Dairy"), 1)
        );
    }

    @Test
    void can_not_delete_category_not_deleted_at_database() {
        doThrow(new DataAccessException("") {
        }).when(jdbcTemplate).update(anyString(), (SqlParameterSource) any());

        assertThrows(
            DataAccessException.class,
            () -> categoryRepository.deleteCategoryById(1)
        );
    }

    @Test
    void can_delete_category_by_id() {
        doReturn(1).when(jdbcTemplate).update(anyString(), (SqlParameterSource) any());

        assertDoesNotThrow(
            () -> categoryRepository.deleteCategoryById(1)
        );
    }

    @Test
    void can_get_categories_list() {
        List<Category> categories = List.of(new Category("Dairy"));
        doReturn(categories).when(jdbcTemplate).query(anyString(), any(RowMapper.class));

        assertDoesNotThrow(
            () -> categoryRepository.getCategories()
        );
        assertEquals(categories, categoryRepository.getCategories());
    }

    @Test
    void can_get_category_by_id() {
        Category category = new Category("Dairy");
        doReturn(List.of(category))
            .when(jdbcTemplate)
            .query(anyString(), (SqlParameterSource) any(), any(RowMapper.class));

        assertDoesNotThrow(
            () -> categoryRepository.getCategoryById(1)
        );
        assertEquals(category, categoryRepository.getCategoryById(1));
    }

    @Test
    void can_get_null_category_by_wrong_id() {
        doReturn(new ArrayList<Category>())
            .when(jdbcTemplate)
            .query(anyString(), (SqlParameterSource) any(), any(RowMapper.class));

        assertDoesNotThrow(
            () -> categoryRepository.getCategoryById(1)
        );
        assertNull(categoryRepository.getCategoryById(1));
    }
}
