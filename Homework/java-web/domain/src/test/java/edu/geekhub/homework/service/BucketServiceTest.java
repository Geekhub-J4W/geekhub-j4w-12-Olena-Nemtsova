package edu.geekhub.homework.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doReturn;

import edu.geekhub.homework.domain.Bucket;
import edu.geekhub.homework.domain.Product;
import edu.geekhub.homework.service.interfaces.OrderService;
import edu.geekhub.homework.service.interfaces.ProductOrderService;
import edu.geekhub.homework.service.interfaces.ProductService;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BucketServiceTest {
    private BucketService bucketService;
    @Mock
    private Bucket bucket;
    @Mock
    private OrderService orderService;
    @Mock
    private ProductService productService;
    @Mock
    private ProductOrderService productOrderService;

    @BeforeEach
    void setUp() {
        bucketService = new BucketService(productService,
            orderService,
            productOrderService,
            bucket);
    }

    @Test
    void can_add_product() {
        Product milk = new Product(1, "Milk", 45.6, 1, null);
        doReturn(milk).when(productService).getProductById(anyInt());
        doReturn(true).when(bucket).addProduct(any());

        boolean addStatus = bucketService.addProduct(milk);

        assertTrue(addStatus);
    }

    @Test
    void can_not_add_product_not_contains_at_product_service() {
        Product milk = new Product(1, "Milk", 45.6, 1, null);
        doReturn(null).when(productService).getProductById(anyInt());

        boolean addStatus = bucketService.addProduct(milk);

        assertFalse(addStatus);
    }

    @Test
    void can_not_add_product_not_added_to_bucket() {
        Product milk = new Product(1, "Milk", 45.6, 1, null);
        doReturn(milk).when(productService).getProductById(anyInt());
        doReturn(false).when(bucket).addProduct(any());

        boolean addStatus = bucketService.addProduct(milk);

        assertFalse(addStatus);
    }

    @Test
    void can_delete_product_by_id() {
        doReturn(true).when(bucket).deleteProduct(anyInt());

        boolean deleteStatus = bucketService.deleteProduct(1);

        assertTrue(deleteStatus);
    }

    @Test
    void can_not_delete_product_by_id_not_deleted_at_bucket() {
        doReturn(false).when(bucket).deleteProduct(anyInt());

        boolean deleteStatus = bucketService.deleteProduct(1);

        assertFalse(deleteStatus);
    }

    @Test
    void can_get_bucket_products() {
        Product milk = new Product(1, "Milk", 45.6, 1, null);
        List<Product> products = List.of(milk);
        doReturn(products).when(bucket).getBucketProducts();

        assertEquals(products, bucketService.getBucketProducts());
    }

    @Test
    void can_checkout_by_bucket() {
        Product milk = new Product(1, "Milk", 45.6, 1, null);
        List<Product> products = List.of(milk);
        doReturn(products).when(bucket).getBucketProducts();
        doReturn(1).when(orderService).addOrder(any());
        doReturn(true).when(productOrderService).addProductOrder(any());
        doReturn(true).when(orderService).updateOrderPriceById(anyDouble(), anyInt());
        doReturn(true).when(orderService).saveToFile(any());

        boolean checkoutStatus = bucketService.checkout();

        assertTrue(checkoutStatus);
    }

    @Test
    void can_not_checkout_by_empty_bucket() {
        List<Product> emptyProducts = new ArrayList<>();
        doReturn(emptyProducts).when(bucket).getBucketProducts();

        boolean checkoutStatus = bucketService.checkout();

        assertFalse(checkoutStatus);
    }

    @Test
    void can_not_checkout_with_not_added_order_to_orderRepo() {
        Product milk = new Product(1, "Milk", 45.6, 1, null);
        List<Product> products = List.of(milk);
        doReturn(products).when(bucket).getBucketProducts();
        doReturn(-1).when(orderService).addOrder(any());

        boolean checkoutStatus = bucketService.checkout();

        assertFalse(checkoutStatus);
    }

    @Test
    void can_not_checkout_with_not_added_productOrder_relations() {
        Product milk = new Product(1, "Milk", 45.6, 1, null);
        List<Product> products = List.of(milk);
        doReturn(products).when(bucket).getBucketProducts();
        doReturn(1).when(orderService).addOrder(any());
        doReturn(true).when(orderService).deleteOrder(anyInt());
        doReturn(false).when(productOrderService).addProductOrder(any());

        boolean checkoutStatus = bucketService.checkout();

        assertFalse(checkoutStatus);
    }
}
