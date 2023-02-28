package edu.geekhub.homework.repository;

import edu.geekhub.homework.domain.Category;
import edu.geekhub.homework.domain.CategoryValidator;
import edu.geekhub.homework.repository.interfaces.CategoryRepository;
import java.util.List;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

public class CategoryRepositoryImpl implements CategoryRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final CategoryValidator categoryValidator;
    private static final String FETCH_ALL_CATEGORIES = """
        SELECT * FROM Categories
        """;
    private static final String INSERT_CATEGORY = """
        INSERT INTO Categories(name) VALUES (:name)
        """;
    private static final String FETCH_CATEGORY_BY_ID = """
        SELECT * FROM Categories WHERE id=:id
        """;
    private static final String DELETE_CATEGORY_BY_ID = """
        DELETE FROM Categories WHERE id=:id
        """;
    private static final String UPDATE_CATEGORY_BY_ID = """
        UPDATE Categories SET name=:name WHERE id=:id
        """;

    public CategoryRepositoryImpl(NamedParameterJdbcTemplate jdbcTemplate,
                                  CategoryValidator categoryValidator) {
        this.jdbcTemplate = jdbcTemplate;
        this.categoryValidator = categoryValidator;
    }

    @Override
    public List<Category> getCategories() {
        return jdbcTemplate.query(FETCH_ALL_CATEGORIES, (rs, rowNum) -> new Category(
            rs.getInt("id"),
            rs.getString("name")
        ));
    }

    @Override
    public void addCategory(Category category) {
        categoryValidator.validate(category);

        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
            .addValue("name", category.name());

        jdbcTemplate.update(INSERT_CATEGORY, mapSqlParameterSource);
    }

    @Override
    public Category getCategoryById(int id) {
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
            .addValue("id", id);

        return jdbcTemplate.query(FETCH_CATEGORY_BY_ID, mapSqlParameterSource,
                (resultSet, rowNum) -> new Category(
                resultSet.getInt("id"),
                resultSet.getString("name")
            ))
            .stream()
            .findFirst()
            .orElse(null);
    }

    @Override
    public boolean deleteCategoryById(int id) {
        Category productToDel = getCategoryById(id);
        if (productToDel != null) {
            MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
                .addValue("id", id);

            jdbcTemplate.update(DELETE_CATEGORY_BY_ID, mapSqlParameterSource);
            return true;
        }
        return false;
    }

    @Override
    public boolean updateCategoryById(Category category, int id) {
        Category productToEdit = getCategoryById(id);
        if (productToEdit != null) {
            categoryValidator.validate(category);

            MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
                .addValue("name", category.name())
                .addValue("id", id);

            jdbcTemplate.update(UPDATE_CATEGORY_BY_ID, mapSqlParameterSource);
            return true;
        }
        return false;
    }
}
