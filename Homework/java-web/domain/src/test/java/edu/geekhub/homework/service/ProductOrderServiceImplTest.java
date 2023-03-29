package edu.geekhub.homework.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import edu.geekhub.homework.domain.Product;
import edu.geekhub.homework.domain.ProductOrder;
import edu.geekhub.homework.domain.ProductOrderValidator;
import edu.geekhub.homework.repository.interfaces.ProductOrderRepository;
import edu.geekhub.homework.service.interfaces.ProductService;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ProductOrderServiceImplTest {
    private ProductOrderServiceImpl productOrderService;
    @Mock
    private ProductOrderRepository productOrderRepository;
    @Mock
    private ProductOrderValidator productOrderValidator;
    @Mock
    private ProductService productService;

    @BeforeEach
    void setUp() {
        productOrderService = new ProductOrderServiceImpl(productOrderRepository,
            productOrderValidator,
            productService);
    }

    @Test
    void can_get_productsOrders_relations() {
        List<ProductOrder> expectedRelations = List.of(new ProductOrder(1, 1));
        when(productOrderRepository.getProductOrders()).thenReturn(expectedRelations);

        List<ProductOrder> relations = productOrderService.getProductOrders();

        assertEquals(expectedRelations, relations);
    }

    @Test
    void can_add_productOrder_relation() {
        ProductOrder relation = new ProductOrder(1, 1);
        doNothing().when(productOrderValidator).validate(any());
        doReturn(1).when(productOrderRepository).addProductOrder(any());
        Product milk = new Product(1, "Milk", 49.5, 1, null, 1);
        doReturn(milk).when(productService).getProductById(anyInt());
        doReturn(milk).when(productService).updateProductById(any(), anyInt());

        boolean successfulAdded = productOrderService.addProductOrder(relation);

        assertTrue(successfulAdded);
    }

    @Test
    void can_not_add_not_valid_productOrder_relation() {
        ProductOrder relation = new ProductOrder(1, 1);
        doThrow(new IllegalArgumentException()).when(productOrderValidator).validate(any());

        boolean successfulAdded = productOrderService.addProductOrder(relation);

        assertFalse(successfulAdded);
    }

    @Test
    void can_not_add_productOrder_relation_not_added_to_repository() {
        ProductOrder relation = new ProductOrder(1, 1);
        doNothing().when(productOrderValidator).validate(any());
        doReturn(-1).when(productOrderRepository).addProductOrder(any());

        boolean successfulAdded = productOrderService.addProductOrder(relation);

        assertFalse(successfulAdded);
    }
}
