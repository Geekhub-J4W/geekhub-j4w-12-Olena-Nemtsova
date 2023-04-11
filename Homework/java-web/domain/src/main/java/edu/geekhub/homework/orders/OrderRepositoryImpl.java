package edu.geekhub.homework.orders;

import edu.geekhub.homework.orders.interfaces.OrderRepository;
import edu.geekhub.homework.products.Product;
import java.util.List;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class OrderRepositoryImpl implements OrderRepository {
    private final OrderValidator validator;
    private final NamedParameterJdbcTemplate jdbcTemplate;
    private static final String FETCH_ALL_ORDERS = """
        SELECT * FROM Orders
        """;
    private static final String FETCH_USER_ORDERS = """
        SELECT * FROM Orders WHERE userId=:userId
        """;
    private static final String INSERT_ORDER = """
        INSERT INTO Orders(date, totalPrice, userId, status, address, customerName)
        VALUES (:date, :totalPrice, :userId, :status, :address, :customerName)
        """;
    private static final String FETCH_ORDER_BY_ID = """
        SELECT * FROM Orders WHERE id=:id
        """;
    private static final String FETCH_COUNT_OF_PRODUCT_BY_ID = """
        SELECT ProductsOrders.productId FROM ProductsOrders
        INNER JOIN Orders ON ProductsOrders.orderID=Orders.id
        WHERE Orders.id=:id AND ProductsOrders.productId=:productId
        """;
    private static final String FETCH_ORDER_PRODUCTS = """
        SELECT * FROM Products
        INNER JOIN ProductsOrders ON Products.id=ProductsOrders.productId
        INNER JOIN Orders ON ProductsOrders.orderID=Orders.id
        WHERE Orders.id=:id
        """;

    private static final String UPDATE_ORDER_PRICE_BY_ID = """
        UPDATE Orders SET totalPrice=:totalPrice WHERE id=:id
        """;

    private static final String UPDATE_ORDER_BY_ID = """
        UPDATE Orders SET
        totalPrice=:totalPrice, status=:status, address=:address, customerName=:customerName
        WHERE id=:id
        """;
    private static final String DELETE_ORDER_BY_ID = """
        DELETE FROM Orders WHERE id=:id
        """;

    public OrderRepositoryImpl(OrderValidator validator, NamedParameterJdbcTemplate jdbcTemplate) {
        this.validator = validator;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Order> getOrders() {
        return jdbcTemplate.query(FETCH_ALL_ORDERS, (rs, rowNum) -> new Order(
            rs.getInt("id"),
            rs.getTimestamp("date").toLocalDateTime(),
            rs.getDouble("totalPrice"),
            rs.getInt("userId"),
            OrderStatus.valueOf(rs.getString("status")),
            rs.getString("address"),
            rs.getString("customerName")
        ));
    }

    @Override
    public List<Order> getOrdersByUserId(int userId) {
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
            .addValue("userId", userId);

        return jdbcTemplate.query(FETCH_USER_ORDERS, mapSqlParameterSource,
            (rs, rowNum) -> new Order(
                rs.getInt("id"),
                rs.getTimestamp("date").toLocalDateTime(),
                rs.getDouble("totalPrice"),
                rs.getInt("userId"),
                OrderStatus.valueOf(rs.getString("status")),
                rs.getString("address"),
                rs.getString("customerName")
            ));
    }

    @Override
    public int addOrder(Order order) {
        validator.validate(order);

        GeneratedKeyHolder generatedKeyHolder = new GeneratedKeyHolder();
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
            .addValue("date", order.getDateTime())
            .addValue("totalPrice", order.getTotalPrice())
            .addValue("userId", order.getUserId())
            .addValue("status", order.getStatus().name())
            .addValue("address", order.getAddress())
            .addValue("customerName", order.getCustomerName());

        jdbcTemplate.update(INSERT_ORDER, mapSqlParameterSource, generatedKeyHolder);

        var keys = generatedKeyHolder.getKeys();
        if (keys != null) {
            return (int) keys.get("id");
        }
        return -1;
    }

    @Override
    public void deleteOrderById(int id) {
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
            .addValue("id", id);

        jdbcTemplate.update(DELETE_ORDER_BY_ID, mapSqlParameterSource);
    }

    @Override
    public Order getOrderById(int id) {
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
            .addValue("id", id);

        return jdbcTemplate.query(FETCH_ORDER_BY_ID, mapSqlParameterSource,
                (rs, rowNum) -> new Order(
                    rs.getInt("id"),
                    rs.getTimestamp("date").toLocalDateTime(),
                    rs.getDouble("totalPrice"),
                    rs.getInt("userId"),
                    OrderStatus.valueOf(rs.getString("status")),
                    rs.getString("address"),
                    rs.getString("customerName")
                ))
            .stream()
            .findFirst()
            .orElse(null);
    }

    @Override
    public List<Product> getOrderProducts(int id) {
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
            .addValue("id", id);

        return jdbcTemplate.query(FETCH_ORDER_PRODUCTS, mapSqlParameterSource,
            (rs, rowNum) -> new Product(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getDouble("price"),
                rs.getInt("categoryId"),
                rs.getBytes("image"),
                rs.getInt("quantity")
            ));
    }

    @Override
    public void updateOrderPriceById(double newPrice, int id) {
        validator.validateTotalPrice(newPrice);

        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
            .addValue("totalPrice", newPrice)
            .addValue("id", id);

        jdbcTemplate.update(UPDATE_ORDER_PRICE_BY_ID, mapSqlParameterSource);
    }

    @Override
    public void updateOrderById(Order order, int id) {
        validator.validate(order);

        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
            .addValue("totalPrice", order.getTotalPrice())
            .addValue("status", order.getStatus().name())
            .addValue("address", order.getAddress())
            .addValue("customerName", order.getCustomerName())
            .addValue("id", id);

        jdbcTemplate.update(UPDATE_ORDER_BY_ID, mapSqlParameterSource);
    }

    @Override
    public int getQuantityOfProductAtOrder(int productId, int orderId) {
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
            .addValue("productId", productId)
            .addValue("id", orderId);

        return jdbcTemplate.query(FETCH_COUNT_OF_PRODUCT_BY_ID, mapSqlParameterSource,
            (rs, rowNum) ->
                rs.getInt("productId")
        ).size();
    }
}

