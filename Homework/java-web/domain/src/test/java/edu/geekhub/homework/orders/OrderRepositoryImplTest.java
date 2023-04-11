package edu.geekhub.homework.orders;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

import edu.geekhub.homework.products.Product;
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
    private final Order order = new Order(LocalDateTime.now(), 1, "");
    @Mock
    private NamedParameterJdbcTemplate jdbcTemplate;
    @Mock
    private OrderValidator validator;

    @BeforeEach
    void setUp() {
        orderRepository = new OrderRepositoryImpl(validator, jdbcTemplate);
    }

    @Test
    void can_not_add_not_valid_order_added_at_database() {
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
    void can_not_update_order_totalPrice_not_updated_at_database() {
        doThrow(new DataAccessException("") {
        }).when(jdbcTemplate).update(anyString(), (SqlParameterSource) any());

        assertThrows(
            DataAccessException.class,
            () -> orderRepository.updateOrderPriceById(-10.0, 1)
        );
    }

    @Test
    void can_update_order_totalPrice() {
        doReturn(1).when(jdbcTemplate).update(anyString(), (SqlParameterSource) any());

        assertDoesNotThrow(
            () -> orderRepository.updateOrderPriceById(10.0, 1)
        );
    }

    @Test
    void can_not_delete_order_not_deleted_at_database() {
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
    void can_get_orders() {
        List<Order> orders = List.of(order);
        doReturn(orders).when(jdbcTemplate).query(anyString(), any(RowMapper.class));
        assertDoesNotThrow(
            () -> orderRepository.getOrders()
        );
        assertEquals(orders, orderRepository.getOrders());
    }

    @Test
    void can_get_orders_by_user_id() {
        List<Order> orders = List.of(order);
        doReturn(orders)
            .when(jdbcTemplate)
            .query(anyString(), (SqlParameterSource) any(), any(RowMapper.class));

        assertDoesNotThrow(
            () -> orderRepository.getOrdersByUserId(1)
        );
        assertEquals(orders, orderRepository.getOrdersByUserId(1));
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
    void can_update_order() {
        doReturn(1).when(jdbcTemplate).update(anyString(), (SqlParameterSource) any());

        assertDoesNotThrow(
            () -> orderRepository.updateOrderById(order, 1)
        );
    }

    @Test
    void can_not_update_order_not_updated_at_database() {
        doThrow(new DataAccessException("") {
        }).when(jdbcTemplate).update(anyString(), (SqlParameterSource) any());

        assertThrows(
            DataAccessException.class,
            () -> orderRepository.updateOrderById(order, 1)
        );
    }

    @Test
    void can_get_count_of_concrete_product_at_order() {
        List<Integer> productsId = List.of(1, 1);
        doReturn(productsId)
            .when(jdbcTemplate)
            .query(anyString(), (SqlParameterSource) any(), any(RowMapper.class));

        assertDoesNotThrow(
            () -> orderRepository.getQuantityOfProductAtOrder(1, 1)
        );
        assertEquals(2, orderRepository.getQuantityOfProductAtOrder(1, 1));
    }
}
