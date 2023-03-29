package edu.geekhub.homework.repository;

import edu.geekhub.homework.domain.Product;
import edu.geekhub.homework.repository.interfaces.BucketRepository;
import java.util.List;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;

public class BucketRepositoryImpl implements BucketRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    private static final String INSERT_BUCKET_PRODUCT = """
        INSERT INTO Buckets(userId, productId) VALUES (:userId, :productId)
        """;
    private static final String FETCH_BUCKET_PRODUCTS_BY_USER_ID = """
        SELECT * FROM Products
        INNER JOIN Buckets ON Products.id=Buckets.productId
        INNER JOIN Users ON Buckets.userId=Users.id
        WHERE Users.id=:userId
        """;
    private static final String DELETE_USER_BUCKETS_BY_ID = """
        DELETE FROM Buckets WHERE userId=:userId
        """;

    private static final String DELETE_USER_BUCKET_PRODUCT_ALL_BY_ID = """
        DELETE FROM Buckets WHERE userId=:userId AND productId=:productId
        """;
    private static final String DELETE_USER_BUCKET_PRODUCT_BY_ID = """
        DELETE FROM Buckets
        WHERE id IN
        (SELECT id FROM Buckets
        WHERE userId=:userId AND productId=:productId
        LIMIT 1)
        """;

    public BucketRepositoryImpl(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public int addBucketProduct(int productId, String userId) {
        GeneratedKeyHolder generatedKeyHolder = new GeneratedKeyHolder();
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
            .addValue("productId", productId)
            .addValue("userId", userId);

        jdbcTemplate.update(INSERT_BUCKET_PRODUCT, mapSqlParameterSource, generatedKeyHolder);

        var keys = generatedKeyHolder.getKeys();
        if (keys != null) {
            return (int) keys.get("id");
        }
        return -1;
    }

    @Override
    public void deleteUserBucketProductsById(String userId) {
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
            .addValue("userId", userId);

        jdbcTemplate.update(DELETE_USER_BUCKETS_BY_ID, mapSqlParameterSource);
    }

    @Override
    public int deleteUserBucketProductById(int productId, String userId) {
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
            .addValue("productId", productId)
            .addValue("userId", userId);

        return jdbcTemplate.update(DELETE_USER_BUCKET_PRODUCT_ALL_BY_ID, mapSqlParameterSource);
    }

    @Override
    public int deleteUserBucketOneProductById(int productId, String userId) {
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
            .addValue("productId", productId)
            .addValue("userId", userId);

        return jdbcTemplate.update(DELETE_USER_BUCKET_PRODUCT_BY_ID, mapSqlParameterSource);
    }


    @Override
    public List<Product> getBucketProductsByUserId(String id) {
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
            .addValue("userId", id);

        return jdbcTemplate.query(FETCH_BUCKET_PRODUCTS_BY_USER_ID, mapSqlParameterSource,
            (resultSet, rowNum) -> new Product(
                resultSet.getInt("id"),
                resultSet.getString("name"),
                resultSet.getDouble("price"),
                resultSet.getInt("categoryId"),
                resultSet.getBytes("image"),
                resultSet.getInt("quantity")
            ));
    }
}
