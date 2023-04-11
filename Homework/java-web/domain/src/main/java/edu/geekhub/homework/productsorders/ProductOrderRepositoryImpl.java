package edu.geekhub.homework.productsorders;

import edu.geekhub.homework.productsorders.interfaces.ProductOrderRepository;
import java.util.List;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class ProductOrderRepositoryImpl implements ProductOrderRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final ProductOrderValidator validator;
    private static final String FETCH_ALL_PRODUCTS_ORDERS = """
        SELECT * FROM ProductsOrders
        """;

    private static final String FETCH_RELATION_BY_PRODUCT_AND_ORDER_ID = """
        SELECT * FROM ProductsOrders
        WHERE productId=:productId AND orderId=:orderId
        """;

    private static final String INSERT_PRODUCT_ORDER = """
        INSERT INTO ProductsOrders(productId, orderId) VALUES (:productId, :orderId)
        """;
    private static final String DELETE_RELATIONS_BY_ORDER_ID = """
        DELETE FROM ProductsOrders WHERE orderId=:orderId
        """;

    public ProductOrderRepositoryImpl(NamedParameterJdbcTemplate jdbcTemplate,
                                      ProductOrderValidator validator) {
        this.jdbcTemplate = jdbcTemplate;
        this.validator = validator;
    }

    @Override
    public List<ProductOrder> getProductOrders() {
        return jdbcTemplate.query(FETCH_ALL_PRODUCTS_ORDERS, (rs, rowNum) -> new ProductOrder(
            rs.getInt("id"),
            rs.getInt("productId"),
            rs.getInt("orderId")
        ));
    }

    @Override
    public ProductOrder getRelationByProductAndOrderId(int productId, int orderId) {
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
            .addValue("productId", productId)
            .addValue("orderId", orderId);

        return jdbcTemplate.query(FETCH_RELATION_BY_PRODUCT_AND_ORDER_ID, mapSqlParameterSource,
                (rs, rowNum) -> new ProductOrder(
                    rs.getInt("id"),
                    rs.getInt("productId"),
                    rs.getInt("orderId")
                ))
            .stream()
            .findFirst()
            .orElse(null);
    }

    @Override
    public int addProductOrder(ProductOrder productOrder) {
        validator.validate(productOrder);

        GeneratedKeyHolder generatedKeyHolder = new GeneratedKeyHolder();
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
            .addValue("productId", productOrder.productId())
            .addValue("orderId", productOrder.orderId());
        jdbcTemplate.update(INSERT_PRODUCT_ORDER, mapSqlParameterSource, generatedKeyHolder);

        var keys = generatedKeyHolder.getKeys();
        if (keys != null) {
            return (int) keys.get("id");
        }
        return -1;
    }

    @Override
    public void deleteRelationsByOrderId(int orderId) {
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
            .addValue("orderId", orderId);

        jdbcTemplate.update(DELETE_RELATIONS_BY_ORDER_ID, mapSqlParameterSource);
    }
}
