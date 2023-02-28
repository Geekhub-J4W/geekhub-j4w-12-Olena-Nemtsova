package edu.geekhub.homework.repository;

import edu.geekhub.homework.domain.ProductOrder;
import edu.geekhub.homework.domain.ProductOrderValidator;
import edu.geekhub.homework.repository.interfaces.ProductOrderRepository;
import java.util.List;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

public class ProductOrderRepositoryImpl implements ProductOrderRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final ProductOrderValidator validator;
    private static final String FETCH_ALL_PRODUCTS_ORDERS = """
        SELECT * FROM ProductsOrders
        """;
    private static final String INSERT_PRODUCT_ORDER = """
        INSERT INTO ProductsOrders(productId, orderId) VALUES (:productId, :orderId)
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
    public void addProductOrder(ProductOrder productOrder) {
        validator.validate(productOrder);

        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
            .addValue("productId", productOrder.productId())
            .addValue("orderId", productOrder.orderId());

        jdbcTemplate.update(INSERT_PRODUCT_ORDER, mapSqlParameterSource);
    }


}
