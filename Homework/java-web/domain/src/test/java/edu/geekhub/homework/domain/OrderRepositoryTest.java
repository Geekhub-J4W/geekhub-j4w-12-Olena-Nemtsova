package edu.geekhub.homework.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OrderRepositoryTest {
    @Mock
    private Order order;
    private OrderRepository orderRepository;

    @BeforeEach
    void setUp() {
        orderRepository = new OrderRepository(new ArrayList<>(), null);
    }

    @Test
    void can_add_order_without_id() {
        OrderRepository orderRepository = spy(this.orderRepository);
        doReturn(true).when(orderRepository).saveToFile(any());
        doReturn(List.of(new Product(1, "Milk", 45.6))).when(order).getOrderProducts();
        doReturn(-1).when(order).getId();

        boolean addStatus = orderRepository.addOrder(order);

        assertTrue(addStatus);
        assertTrue(orderRepository.getOrders().contains(order));
    }

    @Test
    void can_add_order_with_id() {
        OrderRepository orderRepository = spy(this.orderRepository);
        doReturn(true).when(orderRepository).saveToFile(any());
        doReturn(List.of(new Product(1, "Milk", 45.6))).when(order).getOrderProducts();
        doReturn(1).when(order).getId();

        boolean addStatus = orderRepository.addOrder(order);

        assertTrue(addStatus);
        assertTrue(orderRepository.getOrders().contains(order));
    }

    @Test
    void can_not_add_empty_order() {
        doReturn(new ArrayList<>()).when(order).getOrderProducts();

        boolean addStatus = orderRepository.addOrder(order);

        assertFalse(addStatus);
        assertFalse(orderRepository.getOrders().contains(order));
    }

    @Test
    void can_save_order_to_file() throws IOException {
        File tempFile = createTempFile();
        orderRepository = new OrderRepository(new ArrayList<>(), tempFile);
        doReturn(1).when(order).getId();
        String expectedFileData = "Order{id=1, "
            + "products=[Product{id=1, name='Milk', price=45.9}], "
            + "totalPrice=45.9, "
            + "dateTime=21.02.2023 21:59}";
        doReturn(expectedFileData).when(order).toString();

        boolean saveToFileStatus = orderRepository.saveToFile(order);

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
        doReturn(-1).when(order).getId();

        boolean saveToFileStatus = orderRepository.saveToFile(order);

        assertFalse(saveToFileStatus);
    }
}
