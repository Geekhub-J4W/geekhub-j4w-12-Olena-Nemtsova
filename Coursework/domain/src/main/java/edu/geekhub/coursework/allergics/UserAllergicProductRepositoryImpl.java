package edu.geekhub.coursework.allergics;

import edu.geekhub.coursework.allergics.interfaces.UserAllergicProductRepository;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class UserAllergicProductRepositoryImpl implements UserAllergicProductRepository {
    private final UserAllergicProductValidator validator;
    private final NamedParameterJdbcTemplate jdbcTemplate;
    private static final String INSERT_RELATION = """
        INSERT INTO UsersAllergicProducts(productId, userId)
        VALUES (:productId, :userId)
        """;
    private static final String FETCH_RELATION_BY_USER_AND_PRODUCT_ID = """
        SELECT * FROM UsersAllergicProducts
        WHERE userId=:userId AND productId=:productId
        """;
    private static final String DELETE_RELATION_BY_USER_AND_PRODUCT_ID = """
        DELETE FROM UsersAllergicProducts
        WHERE userId=:userId AND productId=:productId
        """;

    public UserAllergicProductRepositoryImpl(UserAllergicProductValidator validator,
                                             NamedParameterJdbcTemplate jdbcTemplate) {
        this.validator = validator;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void addRelation(UserAllergicProduct relation) {
        validator.validate(relation);

        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
            .addValue("productId", relation.getProductId())
            .addValue("userId", relation.getUserId());

        jdbcTemplate.update(INSERT_RELATION, mapSqlParameterSource);
    }

    @Override
    public UserAllergicProduct getRelationByUserAndProductId(int userId, int productId) {
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
            .addValue("userId", userId)
            .addValue("productId", productId);

        return jdbcTemplate.query(FETCH_RELATION_BY_USER_AND_PRODUCT_ID, mapSqlParameterSource,
                (rs, rowNum) -> new UserAllergicProduct(
                    rs.getInt("productId"),
                    rs.getInt("userId")
                ))
            .stream()
            .findFirst()
            .orElse(null);
    }

    @Override
    public void deleteRelationByUserAndProductId(int userId, int productId) {
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
            .addValue("userId", userId)
            .addValue("productId", productId);

        jdbcTemplate.update(DELETE_RELATION_BY_USER_AND_PRODUCT_ID, mapSqlParameterSource);
    }
}
