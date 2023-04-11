package edu.geekhub.homework.categories;

import edu.geekhub.homework.categories.interfaces.CategoryRepository;
import java.util.List;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

@Repository
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
    public int addCategory(Category category) {
        categoryValidator.validate(category);

        GeneratedKeyHolder generatedKeyHolder = new GeneratedKeyHolder();
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
            .addValue("name", category.getName());
        jdbcTemplate.update(INSERT_CATEGORY, mapSqlParameterSource, generatedKeyHolder);

        var keys = generatedKeyHolder.getKeys();
        if (keys != null) {
            return (int) keys.get("id");
        }
        return -1;
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
    public void deleteCategoryById(int id) {
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
            .addValue("id", id);

        jdbcTemplate.update(DELETE_CATEGORY_BY_ID, mapSqlParameterSource);
    }

    @Override
    public void updateCategoryById(Category category, int id) {
        categoryValidator.validate(category);

        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
            .addValue("name", category.getName())
            .addValue("id", id);

        jdbcTemplate.update(UPDATE_CATEGORY_BY_ID, mapSqlParameterSource);
    }
}
