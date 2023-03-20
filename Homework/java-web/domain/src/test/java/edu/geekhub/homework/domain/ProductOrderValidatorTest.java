package edu.geekhub.homework.domain;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doReturn;

import edu.geekhub.homework.repository.interfaces.OrderRepository;
import edu.geekhub.homework.repository.interfaces.ProductRepository;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ProductOrderValidatorTest {
    private ProductOrderValidator productOrderValidator;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private Product product;
    @Mock
    private ProductOrder relation;

    @BeforeEach
    void setUp() {
        productOrderValidator = new ProductOrderValidator(productRepository, orderRepository);
    }

    @Test
    void can_not_validate_null_productOrder() {
        IllegalArgumentException thrown = assertThrows(
            IllegalArgumentException.class,
            () -> productOrderValidator.validate(null)
        );

        assertEquals("ProductOrder was null", thrown.getMessage());
    }

    @Test
    void can_not_validate_productOrder_with_not_exists_product() {
        doReturn(-1).when(relation).productId();
        doReturn(null).when(productRepository).getProductById(anyInt());
        IllegalArgumentException thrown = assertThrows(
            IllegalArgumentException.class,
            () -> productOrderValidator.validate(relation)
        );

        assertEquals("Product with id '-1' doesn't exist", thrown.getMessage());
    }

    @Test
    void can_not_validate_productOrder_with_product_out_of_stock() {
        doReturn(1).when(relation).productId();
        doReturn(0).when(product).getQuantity();
        doReturn(product).when(productRepository).getProductById(anyInt());
        IllegalArgumentException thrown = assertThrows(
            IllegalArgumentException.class,
            () -> productOrderValidator.validate(relation)
        );

        assertEquals("Product with id '1' out of stock", thrown.getMessage());
    }

    @Test
    void can_not_validate_productOrder_with_not_exists_order() {
        doReturn(1).when(relation).productId();
        doReturn(-1).when(relation).orderId();
        doReturn(1).when(product).getQuantity();
        doReturn(product).when(productRepository).getProductById(anyInt());
        doReturn(null).when(orderRepository).getOrderById(anyInt());

        IllegalArgumentException thrown = assertThrows(
            IllegalArgumentException.class,
            () -> productOrderValidator.validate(relation)
        );

        assertEquals("Order with id '-1' doesn't exist", thrown.getMessage());
    }

    @Test
    void can_validate_correct_productOrder() {
        doReturn(1).when(product).getQuantity();
        doReturn(product).when(productRepository).getProductById(anyInt());
        doReturn(new Order(LocalDateTime.now(), null)).when(orderRepository).getOrderById(anyInt());
        ProductOrder relation = new ProductOrder(1, 1);

        assertDoesNotThrow(
            () -> productOrderValidator.validate(relation)
        );
    }
}
