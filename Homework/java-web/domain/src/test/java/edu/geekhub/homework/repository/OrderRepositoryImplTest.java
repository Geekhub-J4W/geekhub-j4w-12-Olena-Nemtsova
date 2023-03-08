package edu.geekhub.homework.repository;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

import edu.geekhub.homework.domain.Order;
import edu.geekhub.homework.domain.OrderStatus;
import edu.geekhub.homework.domain.Product;
import edu.geekhub.homework.domain.User;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

@ExtendWith(MockitoExtension.class)
class OrderRepositoryImplTest {
    private OrderRepositoryImpl orderRepository;
    private final Order order = new Order(LocalDateTime.now(), null);
    @Mock
    private NamedParameterJdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        orderRepository = new OrderRepositoryImpl(jdbcTemplate);
    }

    @Test
    void can_not_add_not_valid_order_at_database_lvl() {
        doThrow(new DataAccessException("") {
        }).when(jdbcTemplate).update(anyString(), any(), any());

        assertThrows(
            DataAccessException.class,
            () -> orderRepository.addOrder(order)
        );
    }

    @Test
    void can_add_product() {
        doReturn(1).when(jdbcTemplate).update(anyString(), any(), any());

        assertDoesNotThrow(
            () -> orderRepository.addOrder(order)
        );
    }

    @Test
    void can_not_update_to_not_valid_order_price_at_database_lvl() {
        doThrow(new DataAccessException("") {
        }).when(jdbcTemplate).update(anyString(), (SqlParameterSource) any());

        assertThrows(
            DataAccessException.class,
            () -> orderRepository.updateOrderPriceById(-10.0, 1)
        );
    }

    @Test
    void can_update_order_price() {
        doReturn(1).when(jdbcTemplate).update(anyString(), (SqlParameterSource) any());

        assertDoesNotThrow(
            () -> orderRepository.updateOrderPriceById(10.0, 1)
        );
    }

    @Test
    void can_not_delete_order_not_exists_at_database_lvl() {
        doThrow(new DataAccessException("") {
        }).when(jdbcTemplate).update(anyString(), (SqlParameterSource) any());

        assertThrows(
            DataAccessException.class,
            () -> orderRepository.deleteOrderById(1)
        );
    }

    @Test
    void can_delete_order_by_id() {
        doReturn(1).when(jdbcTemplate).update(anyString(), (SqlParameterSource) any());

        assertDoesNotThrow(
            () -> orderRepository.deleteOrderById(1)
        );
    }

    @Test
    void can_get_orders_list() {
        List<Order> orders = List.of(order);
        doReturn(orders).when(jdbcTemplate).query(anyString(), any(RowMapper.class));
        assertDoesNotThrow(
            () -> orderRepository.getOrders()
        );
        assertEquals(orders, orderRepository.getOrders());
    }

    @Test
    void can_get_order_by_id() {
        doReturn(List.of(order))
            .when(jdbcTemplate)
            .query(anyString(), (SqlParameterSource) any(), any(RowMapper.class));
        assertDoesNotThrow(
            () -> orderRepository.getOrderById(1)
        );
        assertEquals(order, orderRepository.getOrderById(1));
    }

    @Test
    void can_get_null_order_by_wrong_id() {
        doReturn(new ArrayList<>())
            .when(jdbcTemplate)
            .query(anyString(), (SqlParameterSource) any(), any(RowMapper.class));
        assertDoesNotThrow(
            () -> orderRepository.getOrderById(1)
        );
        assertNull(orderRepository.getOrderById(1));
    }

    @Test
    void can_get_order_products() {
        List<Product> products = List.of(new Product("Milk", 45.6, 1));
        doReturn(products)
            .when(jdbcTemplate)
            .query(anyString(), (SqlParameterSource) any(), any(RowMapper.class));

        assertDoesNotThrow(
            () -> orderRepository.getOrderProducts(1)
        );
        assertEquals(products, orderRepository.getOrderProducts(1));
    }

    @Test
    void can_get_null_customer_by_wrong_id() {
        doReturn(new ArrayList<>())
            .when(jdbcTemplate)
            .query(anyString(), (SqlParameterSource) any(), any(RowMapper.class));
        assertDoesNotThrow(
            () -> orderRepository.getOrderCustomer(1)
        );
        assertNull(orderRepository.getOrderCustomer(1));
    }

    @Test
    void can_get_order_customer() {
        User user = new User();

        doReturn(List.of(user))
            .when(jdbcTemplate)
            .query(anyString(), (SqlParameterSource) any(), any(RowMapper.class));

        assertDoesNotThrow(
            () -> orderRepository.getOrderCustomer(1)
        );
        assertEquals(user, orderRepository.getOrderCustomer(1));
    }

    @Test
    void can_update_order_status() {
        doReturn(1).when(jdbcTemplate).update(anyString(), (SqlParameterSource) any());

        assertDoesNotThrow(
            () -> orderRepository.updateOrderStatus(OrderStatus.PENDING, 1)
        );
    }
}
