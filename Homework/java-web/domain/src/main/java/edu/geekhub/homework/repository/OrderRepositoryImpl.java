package edu.geekhub.homework.repository;

import edu.geekhub.homework.domain.Order;
import edu.geekhub.homework.domain.OrderStatus;
import edu.geekhub.homework.domain.Product;
import edu.geekhub.homework.domain.User;
import edu.geekhub.homework.repository.interfaces.OrderRepository;
import java.util.List;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;

public class OrderRepositoryImpl implements OrderRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;
    private static final String FETCH_ALL_ORDERS = """
        SELECT * FROM Orders
        """;
    private static final String INSERT_ORDER = """
        INSERT INTO Orders(date, totalPrice, userId, status)
        VALUES (:date, :totalPrice, :userId, :status)
        """;
    private static final String FETCH_ORDER_BY_ID = """
        SELECT * FROM Orders WHERE id=:id
        """;
    private static final String FETCH_ORDER_PRODUCTS = """
        SELECT * FROM Products
        INNER JOIN ProductsOrders ON Products.id=ProductsOrders.productId
        INNER JOIN Orders ON ProductsOrders.orderID=Orders.id
        WHERE Orders.id=:id
        """;

    private static final String FETCH_ORDER_CUSTOMER = """
        SELECT * FROM Users
        INNER JOIN Orders ON Users.id=Orders.userId
        WHERE Orders.id=:id
        """;
    private static final String UPDATE_ORDER_BY_ID = """
        UPDATE Orders SET totalPrice=:totalPrice WHERE id=:id
        """;

    private static final String UPDATE_ORDER_STATUS_BY_ID = """
        UPDATE Orders SET status=:status WHERE id=:id
        """;
    private static final String DELETE_ORDER_BY_ID = """
        DELETE FROM Orders WHERE id=:id
        """;

    public OrderRepositoryImpl(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Order> getOrders() {
        return jdbcTemplate.query(FETCH_ALL_ORDERS, (rs, rowNum) -> new Order(
            rs.getInt("id"),
            rs.getTimestamp("date").toLocalDateTime(),
            rs.getDouble("totalPrice"),
            rs.getString("userId"),
            OrderStatus.valueOf(rs.getString("status"))
        ));
    }

    @Override
    public int addOrder(Order order) {
        GeneratedKeyHolder generatedKeyHolder = new GeneratedKeyHolder();
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
            .addValue("date", order.getDateTime())
            .addValue("totalPrice", order.getTotalPrice())
            .addValue("userId", order.getUserId())
            .addValue("status", order.getStatus().name());

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
                (resultSet, rowNum) -> new Order(
                    resultSet.getInt("id"),
                    resultSet.getTimestamp("date").toLocalDateTime(),
                    resultSet.getDouble("totalPrice"),
                    resultSet.getString("userId"),
                    OrderStatus.valueOf(resultSet.getString("status"))
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
                rs.getString("imagePath"),
                rs.getInt("quantity")
            ));
    }

    @Override
    public User getOrderCustomer(int id) {
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
            .addValue("id", id);

        return jdbcTemplate.query(FETCH_ORDER_CUSTOMER, mapSqlParameterSource,
                (rs, rowNum) -> new User(
                    rs.getString("id"),
                    rs.getString("firstName"),
                    rs.getString("lastName"),
                    rs.getString("password"),
                    rs.getString("email"),
                    rs.getBoolean("isAdmin")
                )).stream()
            .findFirst()
            .orElse(null);
    }

    @Override
    public void updateOrderPriceById(double newPrice, int id) {
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
            .addValue("totalPrice", newPrice)
            .addValue("id", id);

        jdbcTemplate.update(UPDATE_ORDER_BY_ID, mapSqlParameterSource);
    }

    @Override
    public void updateOrderStatus(OrderStatus orderStatus, int id) {
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
            .addValue("status", orderStatus.name())
            .addValue("id", id);

        jdbcTemplate.update(UPDATE_ORDER_STATUS_BY_ID, mapSqlParameterSource);
    }
}

