package edu.geekhub.coursework.productsdishes;

import edu.geekhub.coursework.productsdishes.interfaces.ProductDishRepository;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ProductDishRepositoryImpl implements ProductDishRepository {
    private final ProductDishValidator validator;
    private final NamedParameterJdbcTemplate jdbcTemplate;
    private static final String INSERT_RELATION = """
        INSERT INTO ProductsDishes(productQuantity, productId, dishId)
        VALUES (:productQuantity, :productId, :dishId)
        """;
    private static final String FETCH_RELATION_BY_PRODUCT_AND_DISH_ID = """
        SELECT * FROM ProductsDishes
        WHERE productId=:productId AND dishId=:dishId
        """;
    private static final String DELETE_RELATION_BY_PRODUCT_AND_DISH_ID = """
        DELETE FROM ProductsDishes
        WHERE productId=:productId AND dishId=:dishId
        """;
    private static final String UPDATE_RELATION_BY_PRODUCT_AND_DISH_ID = """
        UPDATE ProductsDishes SET
        productQuantity=:productQuantity
        WHERE productId=:productId AND dishId=:dishId
        """;

    public ProductDishRepositoryImpl(
        ProductDishValidator validator,
        NamedParameterJdbcTemplate jdbcTemplate
    ) {
        this.validator = validator;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void addRelation(ProductDish productDish) {
        validator.validate(productDish);

        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
            .addValue("productQuantity", productDish.getProductQuantity())
            .addValue("productId", productDish.getProductId())
            .addValue("dishId", productDish.getDishId());

        jdbcTemplate.update(INSERT_RELATION, mapSqlParameterSource);
    }

    @Override
    public ProductDish getRelationByProductAndDishId(int productId, int dishId) {
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
            .addValue("productId", productId)
            .addValue("dishId", dishId);

        return jdbcTemplate.query(FETCH_RELATION_BY_PRODUCT_AND_DISH_ID, mapSqlParameterSource,
                (rs, rowNum) -> new ProductDish(
                    rs.getInt("productId"),
                    rs.getInt("dishId"),
                    rs.getInt("productQuantity")
                ))
            .stream()
            .findFirst()
            .orElse(null);
    }

    @Override
    public void deleteRelationByProductAndDishId(int productId, int dishId) {
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
            .addValue("productId", productId)
            .addValue("dishId", dishId);

        jdbcTemplate.update(DELETE_RELATION_BY_PRODUCT_AND_DISH_ID, mapSqlParameterSource);
    }

    @Override
    public void updateRelationByProductAndDishId(ProductDish productDish) {
        validator.validate(productDish);

        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
            .addValue("productQuantity", productDish.getProductQuantity())
            .addValue("productId", productDish.getProductId())
            .addValue("dishId", productDish.getDishId());

        jdbcTemplate.update(UPDATE_RELATION_BY_PRODUCT_AND_DISH_ID, mapSqlParameterSource);
    }
}
