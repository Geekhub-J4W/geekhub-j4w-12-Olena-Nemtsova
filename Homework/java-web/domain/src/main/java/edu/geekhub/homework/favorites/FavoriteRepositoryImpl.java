package edu.geekhub.homework.favorites;

import edu.geekhub.homework.favorites.interfaces.FavoriteRepository;
import edu.geekhub.homework.products.Product;
import java.util.List;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class FavoriteRepositoryImpl implements FavoriteRepository {
    private final FavoriteValidator validator;
    private final NamedParameterJdbcTemplate jdbcTemplate;
    private static final String FETCH_FAVORITE_BY_PRODUCT_AND_USER_ID = """
        SELECT * FROM Favorites
        WHERE productId=:productId AND userId=userId
        """;
    private static final String INSERT_FAVORITE = """
        INSERT INTO Favorites(productId, userId)
        VALUES (:productId, :userId)
        """;
    private static final String DELETE_DELETE_BY_PRODUCT_AND_USER_ID = """
        DELETE FROM Favorites
        WHERE productId=:productId AND userId=userId
        """;
    private static final String FETCH_FAVORITE_USER_PRODUCTS = """
        SELECT * FROM Products
        INNER JOIN Favorites
        ON Products.id=Favorites.productId
        WHERE Favorites.userId=:userId
        """;

    public FavoriteRepositoryImpl(FavoriteValidator validator,
                                  NamedParameterJdbcTemplate jdbcTemplate) {
        this.validator = validator;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public int addFavorite(Favorite favorite) {
        validator.validate(favorite);

        GeneratedKeyHolder generatedKeyHolder = new GeneratedKeyHolder();
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
            .addValue("productId", favorite.getProductId())
            .addValue("userId", favorite.getUserId());

        jdbcTemplate.update(INSERT_FAVORITE, mapSqlParameterSource, generatedKeyHolder);

        var keys = generatedKeyHolder.getKeys();
        if (keys != null) {
            return (int) keys.get("id");
        }
        return -1;
    }

    @Override
    public Favorite getFavoriteByProductAndUserId(int productId, int userId) {
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
            .addValue("productId", productId)
            .addValue("userId", userId);

        return jdbcTemplate.query(FETCH_FAVORITE_BY_PRODUCT_AND_USER_ID, mapSqlParameterSource,
                (resultSet, rowNum) -> new Favorite(
                    resultSet.getInt("id"),
                    resultSet.getInt("productId"),
                    resultSet.getInt("userId")
                ))
            .stream()
            .findFirst()
            .orElse(null);
    }

    @Override
    public void deleteFavoriteByProductAndUserId(int productId, int userId) {
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
            .addValue("productId", productId)
            .addValue("userId", userId);

        jdbcTemplate.update(DELETE_DELETE_BY_PRODUCT_AND_USER_ID, mapSqlParameterSource);
    }

    @Override
    public List<Product> getFavoriteUserProducts(int userId) {
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
            .addValue("userId", userId);

        return jdbcTemplate.query(FETCH_FAVORITE_USER_PRODUCTS, mapSqlParameterSource,
            (rs, rowNum) -> new Product(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getDouble("price"),
                rs.getInt("categoryId"),
                rs.getBytes("image"),
                rs.getInt("quantity")
            ));
    }
}
