package edu.geekhub.homework.service;

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
import static org.mockito.Mockito.when;

import edu.geekhub.homework.domain.Order;
import edu.geekhub.homework.domain.OrderStatus;
import edu.geekhub.homework.domain.Product;
import edu.geekhub.homework.domain.User;
import edu.geekhub.homework.repository.interfaces.OrderRepository;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
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

    @BeforeEach
    void setUp() {
        orderService = new OrderServiceImpl(orderRepository, null);
    }

    @Test
    void can_get_orders() {
        List<Order> expectedRelations = List.of(new Order(null, null));
        when(orderRepository.getOrders()).thenReturn(expectedRelations);

        List<Order> relations = orderService.getOrders();

        assertEquals(expectedRelations, relations);
    }

    @Test
    void can_get_order_by_id() {
        Order expectedOrder = new Order(null, null);
        when(orderRepository.getOrderById(anyInt())).thenReturn(expectedOrder);

        Order order = orderService.getOrderById(1);

        assertEquals(expectedOrder, order);
    }

    @Test
    void can_get_null_product_by_wrong_id() {
        when(orderRepository.getOrderById(anyInt())).thenReturn(null);

        Order order = orderService.getOrderById(2);

        assertNull(order);
    }

    @Test
    void can_get_order_products() {
        List<Product> expectedProducts = List.of(new Product(1, "Milk", 45.6, 1, null));
        when(orderRepository.getOrderProducts(anyInt())).thenReturn(expectedProducts);

        List<Product> products = orderService.getOrderProducts(1);

        assertEquals(expectedProducts, products);
    }

    @Test
    void can_add_order() {
        when(orderRepository.addOrder(any())).thenReturn(1);
        int newOrderId = orderService.addOrder(new Order(LocalDateTime.now(), null));

        assertEquals(1, newOrderId);
    }

    @Test
    void can_not_add_order_without_getting_generated_id() {
        when(orderRepository.addOrder(any())).thenReturn(-1);
        int newOrderId = orderService.addOrder(new Order(LocalDateTime.now(), null));

        assertEquals(-1, newOrderId);
    }

    @Test
    void can_not_add_order_not_added_to_repository() {
        doThrow(new DataAccessException("") {
        }).when(orderRepository).addOrder(any());
        int newOrderId = orderService.addOrder(new Order(LocalDateTime.now(), null));

        assertEquals(-1, newOrderId);
    }

    @Test
    void can_delete_order_by_id() {
        Order order = new Order(LocalDateTime.now(), null);
        orderService = spy(orderService);
        doReturn(order).when(orderService).getOrderById(anyInt());
        doNothing().when(orderRepository).deleteOrderById(anyInt());

        boolean deleteStatus = orderService.deleteOrder(1);

        assertTrue(deleteStatus);
    }

    @Test
    void can_not_delete_order_by_wrong_id() {
        orderService = spy(orderService);
        doReturn(null).when(orderService).getOrderById(anyInt());

        boolean deleteStatus = orderService.deleteOrder(1);

        assertFalse(deleteStatus);
    }

    @Test
    void can_not_delete_order_not_deleted_at_repository() {
        Order order = new Order(LocalDateTime.now(), null);
        orderService = spy(orderService);
        doReturn(order).when(orderService).getOrderById(anyInt());
        doThrow(new DataAccessException("") {
        }).when(orderRepository).deleteOrderById(anyInt());

        boolean deleteStatus = orderService.deleteOrder(1);

        assertFalse(deleteStatus);
    }

    @Test
    void can_update_order_totalPrice_by_id() {
        Order order = new Order(LocalDateTime.now(), null);
        when(orderRepository.getOrderById(anyInt())).thenReturn(order);
        doNothing().when(orderRepository).updateOrderPriceById(anyDouble(), anyInt());

        boolean successfulUpdated = orderService.updateOrderPriceById(10, 1);

        assertTrue(successfulUpdated);
    }

    @Test
    void can_not_update_order_totalPrice_by_wrong_id() {
        when(orderRepository.getOrderById(anyInt())).thenReturn(null);

        boolean successfulUpdated = orderService.updateOrderPriceById(10, 1);

        assertFalse(successfulUpdated);
    }

    @Test
    void can_not_update_order_totalPrice_not_updated_at_repository() {
        Order order = new Order(LocalDateTime.now(), null);
        when(orderRepository.getOrderById(anyInt())).thenReturn(order);
        doThrow(new DataAccessException("") {
        }).when(orderRepository).updateOrderPriceById(anyDouble(), anyInt());

        boolean successfulUpdated = orderService.updateOrderPriceById(10, 1);

        assertFalse(successfulUpdated);
    }

    @Test
    void can_save_order_to_file() throws IOException {
        File tempFile = createTempFile();
        orderService = new OrderServiceImpl(orderRepository, tempFile);
        Order order = spy(new Order(null, null));
        when(order.getId()).thenReturn(1);
        String expectedFileData = "Order #1 "
                                  + "Products:"
                                  + "Milk - 45.9"
                                  + "totalPrice: 45.9"
                                  + "date: 27.02.2023 21:59";
        doReturn(expectedFileData).when(order).createCheck(any());

        String gotCheckContent = orderService.saveToFile(order,
            List.of(new Product("Milk", 45.9, 1)));

        BufferedReader reader = new BufferedReader(new FileReader(tempFile));
        String fileData = reader.readLine();
        reader.close();

        assertNotNull(gotCheckContent);
        assertEquals(expectedFileData, fileData);
    }

    private File createTempFile() throws IOException {
        File tempFile = Files.createTempFile("TempCheck", ".txt").toFile();
        tempFile.deleteOnExit();
        return tempFile;
    }

    @Test
    void can_not_save_order_not_contains_at_repository_to_file() {
        Order order = spy(new Order(null, null));
        when(order.getId()).thenReturn(-1);

        String gotCheckContent = orderService.saveToFile(order, new ArrayList<>());

        assertNull(gotCheckContent);
    }

    @Test
    void can_update_order_status_by_id() {
        Order order = new Order(LocalDateTime.now(), null);
        when(orderRepository.getOrderById(anyInt())).thenReturn(order);

        doNothing().when(orderRepository).updateOrderStatus(any(), anyInt());

        Order updated = orderService.updateOrderStatusById(OrderStatus.PENDING, 1);

        assertNotNull(updated);
    }

    @Test
    void can_not_update_order_status_by_wrong_id() {
        when(orderRepository.getOrderById(anyInt())).thenReturn(null);

        Order updated = orderService.updateOrderStatusById(OrderStatus.PENDING, 1);

        assertNull(updated);
    }

    @Test
    void can_not_update_order_status_by_id_not_updated_at_repository() {
        Order order = new Order(LocalDateTime.now(), null);
        when(orderRepository.getOrderById(anyInt())).thenReturn(order);
        doThrow(new DataAccessException("") {
        }).when(orderRepository).updateOrderStatus(any(), anyInt());

        Order updated = orderService.updateOrderStatusById(OrderStatus.PENDING, 1);

        assertNull(updated);
    }

    @Test
    void can_get_order_customer_by_order_id() {
        doReturn(new User()).when(orderRepository).getOrderCustomer(anyInt());

        User customer = orderService.getOrderCustomer(1);

        assertEquals(new User(), customer);
    }
}
