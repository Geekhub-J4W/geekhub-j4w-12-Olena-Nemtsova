package edu.geekhub.homework.reviews;

import edu.geekhub.homework.reviews.interfaces.ReviewRepository;
import java.util.List;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class ReviewRepositoryImpl implements ReviewRepository {
    private final ReviewValidator validator;
    private final NamedParameterJdbcTemplate jdbcTemplate;
    private static final String FETCH_ALL_REVIEWS = """
        SELECT * FROM Reviews
        """;
    private static final String FETCH_REVIEW_BY_PRODUCT_ID = """
        SELECT * FROM Reviews
        WHERE productId=:productId
        """;
    private static final String FETCH_REVIEW_BY_ID = """
        SELECT * FROM Reviews
        WHERE id=:id
        """;
    private static final String FETCH_REVIEW_BY_PRODUCT_AND_ORDER_ID = """
        SELECT * FROM Reviews
        WHERE productId=:productId AND orderId=:orderId
        """;
    private static final String INSERT_REVIEW = """
        INSERT INTO Reviews(text, rating, productId, orderId)
        VALUES (:text, :rating, :productId, :orderId)
        """;
    private static final String UPDATE_REVIEW_BY_ID = """
        UPDATE Reviews SET
        text=:text, rating=:rating, date=:date
        WHERE id=:id
        """;

    public ReviewRepositoryImpl(ReviewValidator validator,
                                NamedParameterJdbcTemplate jdbcTemplate) {
        this.validator = validator;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Review> getReviews() {
        return jdbcTemplate.query(FETCH_ALL_REVIEWS,
            (rs, rowNum) -> new Review(
                rs.getInt("id"),
                rs.getTimestamp("date").toLocalDateTime(),
                rs.getString("text"),
                rs.getInt("rating"),
                rs.getInt("productId"),
                rs.getInt("orderId")
            ));
    }

    @Override
    public List<Review> getReviewsByProductId(int productId) {
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
            .addValue("productId", productId);

        return jdbcTemplate.query(FETCH_REVIEW_BY_PRODUCT_ID, mapSqlParameterSource,
            (rs, rowNum) -> new Review(
                rs.getInt("id"),
                rs.getTimestamp("date").toLocalDateTime(),
                rs.getString("text"),
                rs.getInt("rating"),
                rs.getInt("productId"),
                rs.getInt("orderId")
            ));
    }

    @Override
    public int addReview(Review review) {
        validator.validate(review);

        GeneratedKeyHolder generatedKeyHolder = new GeneratedKeyHolder();
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
            .addValue("text", review.getText())
            .addValue("rating", review.getRating())
            .addValue("productId", review.getProductId())
            .addValue("orderId", review.getOrderId());

        jdbcTemplate.update(INSERT_REVIEW, mapSqlParameterSource, generatedKeyHolder);

        var keys = generatedKeyHolder.getKeys();
        if (keys != null) {
            return (int) keys.get("id");
        }
        return -1;
    }

    @Override
    public Review getReviewByProductAndOrderId(int productId, int orderId) {
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
            .addValue("productId", productId)
            .addValue("orderId", orderId);

        return jdbcTemplate.query(FETCH_REVIEW_BY_PRODUCT_AND_ORDER_ID, mapSqlParameterSource,
                (rs, rowNum) -> new Review(
                    rs.getInt("id"),
                    rs.getTimestamp("date").toLocalDateTime(),
                    rs.getString("text"),
                    rs.getInt("rating"),
                    rs.getInt("productId"),
                    rs.getInt("orderId")
                ))
            .stream()
            .findFirst()
            .orElse(null);
    }

    @Override
    public Review getReviewById(int id) {
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
            .addValue("id", id);

        return jdbcTemplate.query(FETCH_REVIEW_BY_ID, mapSqlParameterSource,
                (rs, rowNum) -> new Review(
                    rs.getInt("id"),
                    rs.getTimestamp("date").toLocalDateTime(),
                    rs.getString("text"),
                    rs.getInt("rating"),
                    rs.getInt("productId"),
                    rs.getInt("orderId")
                ))
            .stream()
            .findFirst()
            .orElse(null);
    }

    @Override
    public void updateReviewById(Review review, int id) {
        validator.validate(review);

        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
            .addValue("date", review.getDateTime())
            .addValue("text", review.getText())
            .addValue("rating", review.getRating())
            .addValue("id", id);

        jdbcTemplate.update(UPDATE_REVIEW_BY_ID, mapSqlParameterSource);
    }
}
