package edu.geekhub.homework.orders;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.spy;

import edu.geekhub.homework.orders.interfaces.OrderRepository;
import edu.geekhub.homework.products.Product;
import edu.geekhub.homework.productsorders.interfaces.ProductOrderService;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessException;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {
    private OrderServiceImpl orderService;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private ProductOrderService productOrderService;
    @Mock
    private OrderValidator orderValidator;
    private Order order;

    @BeforeEach
    void setUp() {
        orderService = new OrderServiceImpl(orderRepository, productOrderService, orderValidator);
        order = new Order(1, LocalDateTime.now(), 100.0, 1,
            OrderStatus.PENDING, "some address", "Mark Pearce");
    }

    @Test
    void can_get_orders() {
        List<Order> expectedOrders = List.of(order);
        doReturn(expectedOrders).when(orderRepository).getOrders();

        List<Order> orders = orderService.getOrders();

        assertEquals(expectedOrders, orders);
    }

    @Test
    void can_get_order_by_id() {
        doReturn(order).when(orderRepository).getOrderById(anyInt());

        Order gotOrder = orderService.getOrderById(1);

        assertEquals(order, gotOrder);
    }

    @Test
    void can_get_null_order_by_wrong_id() {
        doReturn(null).when(orderRepository).getOrderById(anyInt());

        Order gotOrder = orderService.getOrderById(1);

        assertNull(gotOrder);
    }

    @Test
    void can_get_user_orders() {
        List<Order> expectedOrders = List.of(order);
        doReturn(expectedOrders).when(orderRepository).getOrdersByUserId(anyInt());

        List<Order> orders = orderService.getOrdersByUserId(1);

        assertEquals(expectedOrders, orders);
    }

    @Test
    void can_add_order() {
        doReturn(order).when(orderRepository).getOrderById(anyInt());
        doNothing().when(orderValidator).validate(any());
        doReturn(1).when(orderRepository).addOrder(any());

        Order newOrder = orderService.addOrder(order);

        assertNotNull(newOrder);
    }

    @Test
    void can_not_add_not_valid_order() {
        doThrow(new IllegalArgumentException()).when(orderValidator).validate(any());

        Order newOrder = orderService.addOrder(order);

        assertNull(newOrder);
    }

    @Test
    void can_not_add_order_not_get_order_id_from_repository() {
        doNothing().when(orderValidator).validate(any());
        doReturn(-1).when(orderRepository).addOrder(any());

        Order newOrder = orderService.addOrder(order);

        assertNull(newOrder);
    }

    @Test
    void can_not_add_order_not_added_at_repository() {
        doNothing().when(orderValidator).validate(any());
        doThrow(new DataAccessException("") {
        }).when(orderRepository).addOrder(any());
        Order newOrder = orderService.addOrder(order);

        assertNull(newOrder);
    }

    @Test
    void can_delete_order_by_id() {
        doReturn(order).when(orderRepository).getOrderById(anyInt());
        doNothing().when(orderRepository).deleteOrderById(anyInt());

        boolean deleteStatus = orderService.deleteOrder(1);

        assertTrue(deleteStatus);
    }

    @Test
    void can_not_delete_order_by_wrong_id() {
        doReturn(null).when(orderRepository).getOrderById(anyInt());

        boolean deleteStatus = orderService.deleteOrder(1);

        assertFalse(deleteStatus);
    }

    @Test
    void can_not_delete_order_not_deleted_at_repository() {
        doReturn(order).when(orderRepository).getOrderById(anyInt());
        doThrow(new DataAccessException("") {
        }).when(orderRepository).deleteOrderById(anyInt());

        boolean deleteStatus = orderService.deleteOrder(1);

        assertFalse(deleteStatus);
    }

    @Test
    void can_get_order_products() {
        List<Product> expectedProducts = List.of(new Product());
        doReturn(expectedProducts).when(orderRepository).getOrderProducts(anyInt());

        List<Product> products = orderService.getOrderProducts(1);

        assertEquals(expectedProducts, products);
    }


    @Test
    void can_update_order_totalPrice_by_id() {
        doReturn(order).when(orderRepository).getOrderById(anyInt());
        doNothing().when(orderValidator).validateTotalPrice(anyDouble());
        doNothing().when(orderRepository).updateOrderPriceById(anyDouble(), anyInt());

        boolean successfulUpdated = orderService.updateOrderPriceById(10, 1);

        assertTrue(successfulUpdated);
    }

    @Test
    void can_not_update_order_totalPrice_by_wrong_id() {
        doReturn(null).when(orderRepository).getOrderById(anyInt());

        boolean successfulUpdated = orderService.updateOrderPriceById(10, 1);

        assertFalse(successfulUpdated);
    }

    @Test
    void can_not_update_order_totalPrice_to_not_valid() {
        doReturn(order).when(orderRepository).getOrderById(anyInt());
        doThrow(new IllegalArgumentException())
            .when(orderValidator).validateTotalPrice(anyDouble());

        boolean successfulUpdated = orderService.updateOrderPriceById(10, 1);

        assertFalse(successfulUpdated);
    }

    @Test
    void can_not_update_order_totalPrice_not_updated_at_repository() {
        doReturn(order).when(orderRepository).getOrderById(anyInt());
        doNothing().when(orderValidator).validateTotalPrice(anyDouble());
        doThrow(new DataAccessException("") {
        }).when(orderRepository).updateOrderPriceById(anyDouble(), anyInt());

        boolean successfulUpdated = orderService.updateOrderPriceById(10, 1);

        assertFalse(successfulUpdated);
    }


    @Test
    void can_update_order_by_id() {
        doReturn(order).when(orderRepository).getOrderById(anyInt());
        doNothing().when(orderValidator).validate(any());
        doNothing().when(orderRepository).updateOrderById(any(), anyInt());

        Order updated = orderService.updateOrderById(order, 1);

        assertNotNull(updated);
    }

    @Test
    void can_not_update_order_by_wrong_id() {
        doReturn(null).when(orderRepository).getOrderById(anyInt());

        Order updated = orderService.updateOrderById(order, 1);

        assertNull(updated);
    }

    @Test
    void can_not_update_order_by_id_to_not_valid_order() {
        doReturn(order).when(orderRepository).getOrderById(anyInt());
        doThrow(new IllegalArgumentException()).when(orderValidator).validate(any());

        Order updated = orderService.updateOrderById(order, 1);

        assertNull(updated);
    }

    @Test
    void can_not_update_order_by_id_not_updated_at_repository() {
        doReturn(order).when(orderRepository).getOrderById(anyInt());
        doNothing().when(orderValidator).validate(any());
        doThrow(new DataAccessException("") {
        }).when(orderRepository).updateOrderById(any(), anyInt());

        Order updated = orderService.updateOrderById(order, 1);

        assertNull(updated);
    }

    @Test
    void can_get_quantity_of_concrete_product_at_order() {
        doReturn(2).when(orderRepository).getQuantityOfProductAtOrder(anyInt(), anyInt());

        int quantity = orderService.getQuantityOfProductAtOrder(1, 1);

        assertEquals(2, quantity);
    }

    @Test
    void can_add_products_of_order() {
        Product product = new Product(1, "Milk", 49.5, 1, null, 1);
        List<Product> products = new ArrayList<>(List.of(product, product));
        doReturn(true).when(productOrderService).addProductOrder(any());
        doReturn(products).when(orderRepository).getOrderProducts(anyInt());
        orderService = spy(orderService);
        doReturn(true).when(orderService).updateOrderPriceById(anyDouble(), anyInt());

        boolean successfulAdded = orderService.addProductsOfOrder(products, 1);

        assertTrue(successfulAdded);
    }

    @Test
    void can_not_add_null_products_of_order() {
        boolean successfulAdded = orderService.addProductsOfOrder(null, 1);

        assertFalse(successfulAdded);
    }

    @Test
    void can_not_add_empty_products_of_order() {
        boolean successfulAdded = orderService.addProductsOfOrder(new ArrayList<>(), 1);

        assertFalse(successfulAdded);
    }

    @Test
    void can_not_add_products_of_order_not_added_relations() {
        Product product = new Product(1, "Milk", 49.5, 1, null, 1);
        List<Product> products = new ArrayList<>(List.of(product, product));
        doReturn(false).when(productOrderService).addProductOrder(any());
        doReturn(true).when(productOrderService).deleteRelationsByOrderId(anyInt());

        boolean successfulAdded = orderService.addProductsOfOrder(products, 1);

        assertFalse(successfulAdded);
    }

    @Test
    void can_not_add_products_of_order_not_updated_order_totalPrice() {
        Product product = new Product(1, "Milk", 49.5, 1, null, 1);
        List<Product> products = new ArrayList<>(List.of(product, product));
        doReturn(true).when(productOrderService).addProductOrder(any());
        doReturn(products).when(orderRepository).getOrderProducts(anyInt());
        orderService = spy(orderService);
        doReturn(false).when(orderService).updateOrderPriceById(anyDouble(), anyInt());
        doReturn(true).when(productOrderService).deleteRelationsByOrderId(anyInt());

        boolean successfulAdded = orderService.addProductsOfOrder(products, 1);

        assertFalse(successfulAdded);
    }
}
