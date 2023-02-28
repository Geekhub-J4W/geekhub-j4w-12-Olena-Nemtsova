package edu.geekhub.homework.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import edu.geekhub.homework.domain.Order;
import edu.geekhub.homework.domain.Product;
import edu.geekhub.homework.repository.interfaces.OrderRepository;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
        List<Order> expectedRelations = List.of(new Order(null));
        when(orderRepository.getOrders()).thenReturn(expectedRelations);

        List<Order> relations = orderService.getOrders();

        assertEquals(expectedRelations, relations);
    }

    @Test
    void can_get_order_by_id() {
        Order expectedOrder = new Order(null);
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
        int newOrderId = orderService.addOrder(new Order(null));

        assertEquals(1, newOrderId);
    }

    @Test
    void can_delete_order_by_id() {
        when(orderRepository.deleteOrderById(anyInt())).thenReturn(true);

        boolean deleteStatus = orderService.deleteOrder(1);

        assertTrue(deleteStatus);
    }

    @Test
    void can_not_delete_order_by_wrong_id() {
        when(orderRepository.deleteOrderById(anyInt())).thenReturn(false);

        boolean deleteStatus = orderService.deleteOrder(1);

        assertFalse(deleteStatus);
    }

    @Test
    void can_update_order_totalPrice_by_id() {
        Order order = new Order(LocalDateTime.now());
        when(orderRepository.getOrderById(anyInt())).thenReturn(order);

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
    void can_save_order_to_file() throws IOException {
        File tempFile = createTempFile();
        orderService = new OrderServiceImpl(orderRepository, tempFile);
        Order order = spy(new Order(null));
        when(order.id()).thenReturn(1);
        String expectedFileData = "Order{id=1, "
                                  + "products=[Product{id=1, name='Milk', price=45.9}], "
                                  + "totalPrice=45.9, "
                                  + "dateTime=27.02.2023 21:59}";
        doReturn(expectedFileData).when(order).toString();

        boolean saveToFileStatus = orderService.saveToFile(order);

        BufferedReader reader = new BufferedReader(new FileReader(tempFile));
        String fileData = reader.readLine();
        reader.close();

        assertTrue(saveToFileStatus);
        assertEquals(expectedFileData, fileData);
    }

    private File createTempFile() throws IOException {
        File tempFile = Files.createTempFile("TempCheck", ".txt").toFile();
        tempFile.deleteOnExit();
        return tempFile;
    }

    @Test
    void can_not_save_order_not_contains_at_repository_to_file() {
        Order order = spy(new Order(null));
        when(order.id()).thenReturn(-1);

        boolean saveToFileStatus = orderService.saveToFile(order);

        assertFalse(saveToFileStatus);
    }
}
