package edu.geekhub.homework.productsorders;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import edu.geekhub.homework.products.Product;
import edu.geekhub.homework.products.interfaces.ProductService;
import edu.geekhub.homework.productsorders.interfaces.ProductOrderRepository;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessException;

@ExtendWith(MockitoExtension.class)
class ProductOrderServiceImplTest {
    private ProductOrderServiceImpl productOrderService;
    @Mock
    private ProductOrderRepository productOrderRepository;
    @Mock
    private ProductOrderValidator productOrderValidator;
    @Mock
    private ProductService productService;
    private ProductOrder relation;

    @BeforeEach
    void setUp() {
        productOrderService = new ProductOrderServiceImpl(productOrderRepository,
            productOrderValidator,
            productService);

        relation = new ProductOrder(1, 1, 1);
    }

    @Test
    void can_get_productsOrders_relations() {
        List<ProductOrder> expectedRelations = List.of(relation);
        when(productOrderRepository.getProductOrders()).thenReturn(expectedRelations);

        List<ProductOrder> relations = productOrderService.getProductOrders();

        assertEquals(expectedRelations, relations);
    }

    @Test
    void can_add_productOrder_relation() {
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
        doThrow(new IllegalArgumentException()).when(productOrderValidator).validate(any());

        boolean successfulAdded = productOrderService.addProductOrder(relation);

        assertFalse(successfulAdded);
    }

    @Test
    void can_not_add_productOrder_relation_not_get_product_id_from_repository() {
        doNothing().when(productOrderValidator).validate(any());
        doReturn(-1).when(productOrderRepository).addProductOrder(any());

        boolean successfulAdded = productOrderService.addProductOrder(relation);

        assertFalse(successfulAdded);
    }

    @Test
    void can_not_add_productOrder_relation_not_added_at_repository() {
        doNothing().when(productOrderValidator).validate(any());
        doThrow(new DataAccessException("") {
        }).when(productOrderRepository).addProductOrder(any());

        boolean successfulAdded = productOrderService.addProductOrder(relation);

        assertFalse(successfulAdded);
    }

    @Test
    void can_delete_productOrder_relations_by_order_id() {
        doNothing().when(productOrderValidator).validateOrderExists(anyInt());
        doReturn(List.of(relation)).when(productOrderRepository).getProductOrders();
        doNothing().when(productOrderRepository).deleteRelationsByOrderId(anyInt());
        Product milk = new Product(1, "Milk", 49.5, 1, null, 1);
        doReturn(milk).when(productService).getProductById(anyInt());
        doReturn(milk).when(productService).updateProductById(any(), anyInt());

        boolean successfulDeleted = productOrderService.deleteRelationsByOrderId(1);

        assertTrue(successfulDeleted);
    }

    @Test
    void can_not_delete_productOrder_relations_by_not_existing_order_id() {
        doThrow(new IllegalArgumentException())
            .when(productOrderValidator).validateOrderExists(anyInt());

        boolean successfulDeleted = productOrderService.deleteRelationsByOrderId(1);

        assertFalse(successfulDeleted);
    }

    @Test
    void can_not_delete_productOrder_relations_not_deleted_at_repository() {
        doNothing().when(productOrderValidator).validateOrderExists(anyInt());
        doReturn(new ArrayList<>()).when(productOrderRepository).getProductOrders();
        doThrow(new DataAccessException("") {
        }).when(productOrderRepository).deleteRelationsByOrderId(anyInt());

        boolean successfulDeleted = productOrderService.deleteRelationsByOrderId(1);

        assertFalse(successfulDeleted);
    }
}
