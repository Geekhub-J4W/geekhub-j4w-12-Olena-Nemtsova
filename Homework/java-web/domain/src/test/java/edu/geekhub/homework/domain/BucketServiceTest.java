package edu.geekhub.homework.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BucketServiceTest {
    @Mock
    private Bucket bucket;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private ProductService productService;
    private BucketService bucketService;

    @BeforeEach
    void setUp() {
        bucketService = new BucketService(productService, orderRepository, bucket);
    }

    @Test
    void can_add_product() {
        when(productService.containsProduct(any())).thenReturn(true);
        when(bucket.addProduct(any())).thenReturn(true);
        Product milk = new Product(1, "Milk", 45.6);

        boolean addStatus = bucketService.addProduct(milk);

        assertTrue(addStatus);
    }

    @Test
    void can_not_add_product_not_contains_at_product_service() {
        when(productService.containsProduct(any())).thenReturn(false);
        Product milk = new Product(1, "Milk", 45.6);

        boolean addStatus = bucketService.addProduct(milk);

        assertFalse(addStatus);
    }

    @Test
    void can_not_add_product_not_added_to_bucket() {
        when(productService.containsProduct(any())).thenReturn(true);
        when(bucket.addProduct(any())).thenReturn(false);
        Product milk = new Product(1, "Milk", 45.6);

        boolean addStatus = bucketService.addProduct(milk);

        assertFalse(addStatus);
    }

    @Test
    void can_delete_product_by_id() {
        when(bucket.deleteProduct(anyInt())).thenReturn(true);

        boolean deleteStatus = bucketService.deleteProduct(1);

        assertTrue(deleteStatus);
    }

    @Test
    void can_not_delete_product_by_id_not_deleted_at_bucket() {
        when(bucket.deleteProduct(anyInt())).thenReturn(false);

        boolean deleteStatus = bucketService.deleteProduct(1);

        assertFalse(deleteStatus);
    }

    @Test
    void can_get_bucket_products() {
        Product milk = new Product(1, "Milk", 45.6);
        List<Product> products = List.of(milk);
        when(bucket.getBucketProducts()).thenReturn(products);

        assertEquals(products, bucketService.getBucketProducts());
    }

    @Test
    void can_checkout_by_bucket() {
        Product milk = new Product(1, "Milk", 45.6);
        List<Product> products = List.of(milk);
        when(bucket.getBucketProducts()).thenReturn(products);
        when(orderRepository.addOrder(any())).thenReturn(true);

        boolean checkoutStatus = bucketService.checkout();

        assertTrue(checkoutStatus);
    }

    @Test
    void can_not_checkout_by_empty_bucket() {
        List<Product> emptyProducts = new ArrayList<>();
        when(bucket.getBucketProducts()).thenReturn(emptyProducts);

        boolean checkoutStatus = bucketService.checkout();

        assertFalse(checkoutStatus);
    }

    @Test
    void can_not_checkout_by_bucket_not_added_order_to_orderRepo() {
        Product milk = new Product(1, "Milk", 45.6);
        List<Product> products = List.of(milk);
        when(bucket.getBucketProducts()).thenReturn(products);
        when(orderRepository.addOrder(any())).thenReturn(false);

        boolean checkoutStatus = bucketService.checkout();

        assertFalse(checkoutStatus);
    }
}
