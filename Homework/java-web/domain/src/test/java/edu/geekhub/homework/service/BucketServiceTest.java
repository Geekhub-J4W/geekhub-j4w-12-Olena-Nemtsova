package edu.geekhub.homework.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doReturn;

import edu.geekhub.homework.domain.Product;
import edu.geekhub.homework.repository.interfaces.BucketRepository;
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
    private BucketRepository bucketRepository;
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
            bucketRepository);
    }

    @Test
    void can_add_product() {
        Product milk = new Product(1, "Milk", 45.6, 1, null);
        doReturn(milk).when(productService).getProductById(anyInt());
        doReturn(1).when(bucketRepository).addBucketProduct(anyInt(), any());

        boolean addStatus = bucketService.addProduct(milk, "userId1");

        assertTrue(addStatus);
    }

    @Test
    void can_not_add_product_not_contains_at_product_service() {
        Product milk = new Product(1, "Milk", 45.6, 1, null);
        doReturn(null).when(productService).getProductById(anyInt());

        boolean addStatus = bucketService.addProduct(milk, "userId1");

        assertFalse(addStatus);
    }

    @Test
    void can_not_add_product_not_added_to_bucket() {
        Product milk = new Product(1, "Milk", 45.6, 1, null);
        doReturn(milk).when(productService).getProductById(anyInt());
        doReturn(-1).when(bucketRepository).addBucketProduct(anyInt(), any());

        boolean addStatus = bucketService.addProduct(milk, "userId1");

        assertFalse(addStatus);
    }

    @Test
    void can_delete_product_by_id() {
        doReturn(1).when(bucketRepository).deleteUserBucketProductById(anyInt(), any());

        boolean deleteStatus = bucketService.deleteProduct(1, "userId1");

        assertTrue(deleteStatus);
    }

    @Test
    void can_not_delete_product_by_id_not_deleted_at_bucket() {
        doReturn(0).when(bucketRepository).deleteUserBucketProductById(anyInt(), any());

        boolean deleteStatus = bucketService.deleteProduct(1, "userId1");

        assertFalse(deleteStatus);
    }

    @Test
    void can_get_bucket_products() {
        Product milk = new Product(1, "Milk", 45.6, 1, null);
        List<Product> products = List.of(milk);
        doReturn(products).when(bucketRepository).getBucketProductsByUserId(any());

        assertEquals(products, bucketService.getBucketProducts("userId1"));
    }

    @Test
    void can_checkout_by_bucket() {
        Product milk = new Product(1, "Milk", 45.6, 1, null);
        List<Product> products = List.of(milk);
        doReturn(products).when(bucketRepository).getBucketProductsByUserId(any());
        doReturn(1).when(orderService).addOrder(any());
        doReturn(true).when(productOrderService).addProductOrder(any());
        doReturn(true).when(orderService).updateOrderPriceById(anyDouble(), anyInt());
        doReturn("some check").when(orderService).saveToFile(any(), any());

        String gotCheckContent = bucketService.checkout("userId1");

        assertNotNull(gotCheckContent);
    }

    @Test
    void can_not_checkout_by_empty_bucket() {
        List<Product> emptyProducts = new ArrayList<>();
        doReturn(emptyProducts).when(bucketRepository).getBucketProductsByUserId(any());

        String gotCheckContent = bucketService.checkout("userId1");

        assertNull(gotCheckContent);
    }

    @Test
    void can_not_checkout_with_not_added_order_to_orderRepo() {
        Product milk = new Product(1, "Milk", 45.6, 1, null);
        List<Product> products = List.of(milk);
        doReturn(products).when(bucketRepository).getBucketProductsByUserId(any());
        doReturn(-1).when(orderService).addOrder(any());

        String gotCheckContent = bucketService.checkout("userId1");

        assertNull(gotCheckContent);
    }

    @Test
    void can_not_checkout_with_not_added_productOrder_relations() {
        Product milk = new Product(1, "Milk", 45.6, 1, null);
        List<Product> products = List.of(milk);
        doReturn(products).when(bucketRepository).getBucketProductsByUserId(any());
        doReturn(1).when(orderService).addOrder(any());
        doReturn(true).when(orderService).deleteOrder(anyInt());
        doReturn(false).when(productOrderService).addProductOrder(any());

        String gotCheckContent = bucketService.checkout("userId1");

        assertNull(gotCheckContent);
    }

    @Test
    void can_add_product_by_id() {
        Product milk = new Product(1, "Milk", 45.6, 1, null);
        doReturn(milk).when(productService).getProductById(anyInt());
        doReturn(1).when(bucketRepository).addBucketProduct(anyInt(), any());

        boolean addStatus = bucketService.addProductById(1, "userId1");

        assertTrue(addStatus);
    }

    @Test
    void can_not_add_product_by_id_not_contains_at_product_service() {
        Product milk = new Product(1, "Milk", 45.6, 1, null);
        doReturn(null).when(productService).getProductById(anyInt());

        boolean addStatus = bucketService.addProduct(milk, "userId1");

        assertFalse(addStatus);
    }

    @Test
    void can_not_add_product_by_id_not_added_to_bucket_repository() {
        Product milk = new Product(1, "Milk", 45.6, 1, null);
        doReturn(milk).when(productService).getProductById(anyInt());
        doReturn(-1).when(bucketRepository).addBucketProduct(anyInt(), any());

        boolean addStatus = bucketService.addProductById(1, "userId1");

        assertFalse(addStatus);
    }

    @Test
    void can_get_bucket_total_price_by_user_id() {
        Product milk = new Product(1, "Milk", 45, 1, null);
        doReturn(List.of(milk, milk)).when(bucketRepository).getBucketProductsByUserId(any());

        double totalPrice = bucketService.getBucketTotalPrice("userId1");

        assertEquals(90, totalPrice);
    }

    @Test
    void can_get_zero_bucket_total_price_by_wrong_user_id() {
        doReturn(new ArrayList<Product>()).when(bucketRepository).getBucketProductsByUserId(any());

        double totalPrice = bucketService.getBucketTotalPrice("userId1");

        assertEquals(0, totalPrice);
    }
}
