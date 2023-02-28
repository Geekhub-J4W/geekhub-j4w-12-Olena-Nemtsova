package edu.geekhub.homework.repository;

import edu.geekhub.homework.domain.Product;
import edu.geekhub.homework.domain.ProductValidator;
import edu.geekhub.homework.repository.interfaces.ProductRepository;
import java.util.List;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;

public class ProductRepositoryImpl implements ProductRepository {
    private final ProductValidator productValidator;
    private final NamedParameterJdbcTemplate jdbcTemplate;
    private static final String FETCH_ALL_PRODUCTS = """
        SELECT * FROM Products
        """;
    private static final String INSERT_PRODUCT = """
        INSERT INTO Products(name, price, categoryId, imagePath)
        VALUES (:name, :price, :categoryId, :imagePath)
        """;
    private static final String FETCH_PRODUCT_BY_ID = """
        SELECT * FROM Products WHERE id=:id
        """;
    private static final String DELETE_PRODUCT_BY_ID = """
        DELETE FROM Products WHERE id=:id
        """;
    private static final String UPDATE_PRODUCT_BY_ID = """
        UPDATE Products SET name=:name, price=:price, categoryId=:categoryId, imagePath=:imagePath
        WHERE id=:id
        """;
    private static final String FETCH_PRODUCTS_RATING_SORTED = """
        SELECT * FROM Products
        ORDER BY
        (SELECT COUNT(ProductsOrders.productId)
        FROM ProductsOrders WHERE ProductsOrders.productId = Products.id)
        DESC
        """;

    public ProductRepositoryImpl(NamedParameterJdbcTemplate jdbcTemplate,
                                 ProductValidator productValidator) {
        this.jdbcTemplate = jdbcTemplate;
        this.productValidator = productValidator;
    }

    @Override
    public List<Product> getProducts() {
        return jdbcTemplate.query(FETCH_ALL_PRODUCTS,
            (rs, rowNum) -> new Product(
            rs.getInt("id"),
            rs.getString("name"),
            rs.getDouble("price"),
            rs.getInt("categoryId"),
            rs.getString("imagePath")
        ));
    }

    @Override
    public List<Product> getProductsRatingSorted() {
        return jdbcTemplate.query(FETCH_PRODUCTS_RATING_SORTED,
            (rs, rowNum) -> new Product(
            rs.getInt("id"),
            rs.getString("name"),
            rs.getDouble("price"),
            rs.getInt("categoryId"),
            rs.getString("imagePath")
        ));
    }

    @Override
    public int addProduct(Product product) {
        productValidator.validate(product);

        GeneratedKeyHolder generatedKeyHolder = new GeneratedKeyHolder();
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
            .addValue("name", product.name())
            .addValue("price", product.price())
            .addValue("categoryId", product.categoryId())
            .addValue("imagePath", product.imagePath());

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
                (resultSet, rowNum) -> new Product(
                resultSet.getInt("id"),
                resultSet.getString("name"),
                resultSet.getDouble("price"),
                resultSet.getInt("categoryId"),
                resultSet.getString("imagePath")
            ))
            .stream()
            .findFirst()
            .orElse(null);
    }

    @Override
    public boolean deleteProductById(int id) {
        Product productToDel = getProductById(id);
        if (productToDel != null) {
            MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
                .addValue("id", id);

            jdbcTemplate.update(DELETE_PRODUCT_BY_ID, mapSqlParameterSource);
            return true;
        }
        return false;
    }

    @Override
    public boolean updateProductById(Product product, int id) {
        Product productToEdit = getProductById(id);
        if (productToEdit != null) {
            productValidator.validate(product);

            MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
                .addValue("name", product.name())
                .addValue("price", product.price())
                .addValue("categoryId", product.categoryId())
                .addValue("id", id)
                .addValue("imagePath", product.imagePath());

            jdbcTemplate.update(UPDATE_PRODUCT_BY_ID, mapSqlParameterSource);
            return true;
        }
        return false;
    }
}
