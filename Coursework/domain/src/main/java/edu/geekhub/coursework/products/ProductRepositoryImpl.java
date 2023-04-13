package edu.geekhub.coursework.products;

import edu.geekhub.coursework.products.interfaces.ProductRepository;
import java.util.List;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class ProductRepositoryImpl implements ProductRepository {
    private final ProductValidator validator;
    private final NamedParameterJdbcTemplate jdbcTemplate;
    private static final String FETCH_ALL_PRODUCTS = """
        SELECT * FROM Products
        """;
    private static final String INSERT_PRODUCT = """
        INSERT INTO Products(name, calories)
        VALUES (:name, :calories)
        """;
    private static final String FETCH_PRODUCT_BY_ID = """
        SELECT * FROM Products WHERE id=:id
        """;
    private static final String DELETE_PRODUCT_BY_ID = """
        DELETE FROM Products WHERE id=:id
        """;
    private static final String UPDATE_PRODUCT_BY_ID = """
        UPDATE Products SET
        name=:name, calories=:calories
        WHERE id=:id
        """;

    private static final String FETCH_PRODUCTS_OF_DISH = """
        SELECT * FROM Products
        INNER JOIN ProductsDishes
        ON Products.id=ProductsDishes.productId
        WHERE ProductsDishes.dishId=:dishId
        """;
    private static final String FETCH_ALLERGIC_USER_PRODUCTS = """
        SELECT * FROM Products
        INNER JOIN UsersAllergicProducts
        ON Products.id=UsersAllergicProducts.productId
        WHERE Users.id=:userId
        """;
    private static final String FETCH_PRODUCTS_NAME_SORTED_BY_PAGE_AND_INPUT = """
        SELECT * FROM Products
        WHERE Products.name ILIKE :input
        ORDER BY Products.name
        LIMIT :limit
        OFFSET :limit * :pageNumber
        """;

    public ProductRepositoryImpl(NamedParameterJdbcTemplate jdbcTemplate,
                                 ProductValidator validator) {
        this.jdbcTemplate = jdbcTemplate;
        this.validator = validator;
    }

    @Override
    public List<Product> getProducts() {
        return jdbcTemplate.query(FETCH_ALL_PRODUCTS,
            (rs, rowNum) -> new Product(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getInt("calories")
            ));
    }

    @Override
    public int addProduct(Product product) {
        validator.validate(product);

        GeneratedKeyHolder generatedKeyHolder = new GeneratedKeyHolder();
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
            .addValue("name", product.getName())
            .addValue("calories", product.getCalories());

        jdbcTemplate.update(INSERT_PRODUCT, mapSqlParameterSource, generatedKeyHolder);

        var keys = generatedKeyHolder.getKeys();
        if (keys != null) {
            return (int) keys.get("id");
        }
        return -1;
    }

    @Override
    public Product getProductById(int id) {
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
            .addValue("id", id);

        return jdbcTemplate.query(FETCH_PRODUCT_BY_ID, mapSqlParameterSource,
                (rs, rowNum) -> new Product(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getInt("calories")
                ))
            .stream()
            .findFirst()
            .orElse(null);
    }

    @Override
    public void deleteProductById(int id) {
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
            .addValue("id", id);

        jdbcTemplate.update(DELETE_PRODUCT_BY_ID, mapSqlParameterSource);
    }

    @Override
    public void updateProductById(Product product, int id) {
        validator.validate(product);

        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
            .addValue("name", product.getName())
            .addValue("calories", product.getCalories())
            .addValue("id", id);

        jdbcTemplate.update(UPDATE_PRODUCT_BY_ID, mapSqlParameterSource);
    }

    @Override
    public List<Product> getProductsByDishId(int dishId) {
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
            .addValue("dishId", dishId);

        return jdbcTemplate.query(FETCH_PRODUCTS_OF_DISH, mapSqlParameterSource,
            (rs, rowNum) -> new Product(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getInt("calories")
            ));
    }

    @Override
    public List<Product> getProductsByUserId(int userId) {
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
            .addValue("userId", userId);

        return jdbcTemplate.query(FETCH_ALLERGIC_USER_PRODUCTS, mapSqlParameterSource,
            (rs, rowNum) -> new Product(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getInt("calories")
            ));
    }

    @Override
    public List<Product> getProductsNameSortedByPageAndInput(
        int limit,
        int pageNumber,
        String input
    ) {
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
            .addValue("limit", limit)
            .addValue("pageNumber", pageNumber - 1)
            .addValue("input", "%" + input + "%");

        return jdbcTemplate.query(FETCH_PRODUCTS_NAME_SORTED_BY_PAGE_AND_INPUT,
            mapSqlParameterSource,
            (rs, rowNum) -> new Product(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getInt("calories")
            ));
    }
}
