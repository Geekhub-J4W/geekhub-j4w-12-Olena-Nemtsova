package edu.geekhub.homework.buckets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

import edu.geekhub.homework.orders.Order;
import edu.geekhub.homework.orders.OrderStatus;
import edu.geekhub.homework.orders.interfaces.OrderService;
import edu.geekhub.homework.products.Product;
import edu.geekhub.homework.products.interfaces.ProductRepository;
import java.time.LocalDateTime;
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
    private ProductRepository productRepository;
    private Product product;
    private Order order;


    @BeforeEach
    void setUp() {
        bucketService = new BucketService(bucketRepository,
            productRepository,
            orderService);

        product = spy(
            new Product(1, "Milk", 49.5, 1, null, 1)
        );
        order = new Order(1, LocalDateTime.now(), 100.0, 1,
            OrderStatus.PENDING, "some address", "Mark Pearce");
    }

    @Test
    void can_add_product() {
        doReturn(product).when(productRepository).getProductById(anyInt());
        doReturn(1).when(bucketRepository).addBucketProduct(anyInt(), anyInt());

        boolean addStatus = bucketService.addProduct(1, 1);

        assertTrue(addStatus);
    }

    @Test
    void can_not_add_not_exists_product() {
        doReturn(null).when(productRepository).getProductById(anyInt());

        boolean addStatus = bucketService.addProduct(1, 1);

        assertFalse(addStatus);
    }

    @Test
    void can_not_add_product_with_zero_quantity_to_bucket() {
        doReturn(product).when(productRepository).getProductById(anyInt());
        doReturn(0).when(product).getQuantity();

        boolean addStatus = bucketService.addProduct(1, 1);

        assertFalse(addStatus);
    }

    @Test
    void can_not_add_product_not_added_at_bucket_repository() {
        doReturn(product).when(productRepository).getProductById(anyInt());
        doReturn(-1).when(bucketRepository).addBucketProduct(anyInt(), anyInt());

        boolean addStatus = bucketService.addProduct(1, 1);

        assertFalse(addStatus);
    }

    @Test
    void can_delete_all_concrete_products_by_id() {
        doReturn(1).when(bucketRepository).deleteUserBucketProductById(anyInt(), anyInt());

        boolean deleteStatus = bucketService.deleteAllConcreteProducts(1, 1);

        assertTrue(deleteStatus);
    }

    @Test
    void can_not_delete_all_concrete_products_by_id_not_deleted_at_repository() {
        doReturn(0).when(bucketRepository).deleteUserBucketProductById(anyInt(), anyInt());

        boolean deleteStatus = bucketService.deleteAllConcreteProducts(1, 1);

        assertFalse(deleteStatus);
    }

    @Test
    void can_delete_only_one_product_by_id() {
        doReturn(1).when(bucketRepository).deleteUserBucketOneProductById(anyInt(), anyInt());

        boolean deleteStatus = bucketService.deleteOneProduct(1, 1);

        assertTrue(deleteStatus);
    }

    @Test
    void can_not_delete_one_product_by_id_not_deleted_at_repository() {
        doReturn(0).when(bucketRepository).deleteUserBucketOneProductById(anyInt(), anyInt());

        boolean deleteStatus = bucketService.deleteOneProduct(1, 1);

        assertFalse(deleteStatus);
    }

    @Test
    void can_get_bucket_products() {
        List<Product> products = List.of(product);
        doReturn(products).when(bucketRepository).getBucketProductsByUserId(anyInt());

        assertEquals(products, bucketService.getBucketProducts(1));
    }

    @Test
    void can_get_bucket_total_price_by_user_id() {
        doReturn(List.of(product, product))
            .when(bucketRepository).getBucketProductsByUserId(anyInt());

        double totalPrice = bucketService.getBucketTotalPrice(1);

        assertEquals(99, totalPrice);
    }

    @Test
    void can_get_zero_bucket_total_price_by_wrong_user_id() {
        doReturn(new ArrayList<Product>())
            .when(bucketRepository).getBucketProductsByUserId(anyInt());

        double totalPrice = bucketService.getBucketTotalPrice(1);

        assertEquals(0, totalPrice);
    }

    @Test
    void can_checkout_by_bucket_products() {
        List<Product> products = List.of(product);
        doReturn(products).when(bucketRepository).getBucketProductsByUserId(anyInt());
        doReturn(order).when(orderService).addOrder(any());
        doReturn(true).when(orderService).addProductsOfOrder(any(), anyInt());
        doNothing().when(bucketRepository).deleteUserBucketProductsById(anyInt());

        boolean checkoutSuccess = bucketService.checkout(1, order);

        assertTrue(checkoutSuccess);
    }

    @Test
    void can_not_checkout_null_order_by_bucket_products() {
        boolean checkoutSuccess = bucketService.checkout(1, null);

        assertFalse(checkoutSuccess);
    }

    @Test
    void can_not_checkout_by_empty_bucket() {
        List<Product> emptyProducts = new ArrayList<>();
        doReturn(emptyProducts).when(bucketRepository).getBucketProductsByUserId(anyInt());

        boolean checkoutSuccess = bucketService.checkout(1, order);

        assertFalse(checkoutSuccess);
    }

    @Test
    void can_not_checkout_with_not_added_order() {
        List<Product> products = List.of(product);
        doReturn(products).when(bucketRepository).getBucketProductsByUserId(anyInt());
        doReturn(null).when(orderService).addOrder(any());

        boolean checkoutSuccess = bucketService.checkout(1, order);

        assertFalse(checkoutSuccess);
    }

    @Test
    void can_not_checkout_with_not_added_order_products() {
        List<Product> products = List.of(product);
        doReturn(products).when(bucketRepository).getBucketProductsByUserId(anyInt());
        doReturn(order).when(orderService).addOrder(any());
        doReturn(false).when(orderService).addProductsOfOrder(any(), anyInt());
        doReturn(true).when(orderService).deleteOrder(anyInt());

        boolean checkoutSuccess = bucketService.checkout(1, order);

        assertFalse(checkoutSuccess);
    }

    @Test
    void can_get_count_of_product_at_user_bucket() {
        doReturn(List.of(product, product))
            .when(bucketRepository).getBucketProductsByUserId(anyInt());

        int count = bucketService.getCountOfConcreteProductAtBucket(1, 1);

        assertEquals(2, count);
    }
}
