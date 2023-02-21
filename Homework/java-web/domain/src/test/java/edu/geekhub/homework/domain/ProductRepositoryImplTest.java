package edu.geekhub.homework.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ProductRepositoryImplTest {
    private ProductRepository productRepository;
    @Mock
    private ProductValidator productValidator;

    @BeforeEach
    void setUp() {
        productRepository = new ProductRepositoryImpl(new ArrayList<>(), productValidator);
    }

    @Test
    void can_get_products() throws Exception {
        List<Product> expectedProducts = List.of(new Product(1, "Milk", 45.6));
        Field field = ProductRepositoryImpl.class.getDeclaredField("products");
        field.setAccessible(true);
        field.set(productRepository, expectedProducts);

        List<Product> products = productRepository.getProducts();

        assertEquals(expectedProducts, products);
    }

    @Test
    void can_not_add_not_valid_product() {
        doThrow(new IllegalArgumentException()).when(productValidator).validate(any());

        assertThrows(
            IllegalArgumentException.class,
            () -> productRepository.addProduct(null)
        );
    }

    @Test
    void can_add_product_without_id() {
        doNothing().when(productValidator).validate(any());
        productRepository.addProduct(new Product("Milk", 45.6));

        List<Product> expectedProducts = List.of(new Product(1, "Milk", 45.6));

        assertEquals(expectedProducts, productRepository.getProducts());
    }

    @Test
    void can_add_product_with_id() {
        doNothing().when(productValidator).validate(any());
        productRepository.addProduct(new Product(1, "Milk", 45.6));

        List<Product> expectedProducts = List.of(new Product(1, "Milk", 45.6));

        assertEquals(expectedProducts, productRepository.getProducts());
    }

    @Test
    void can_add_product_without_id_after_added_with_id() {
        doNothing().when(productValidator).validate(any());
        productRepository.addProduct(new Product(1, "Milk", 45.6));
        productRepository.addProduct(new Product("Bread", 12.5));

        List<Product> expectedProducts = List.of(new Product(1, "Milk", 45.6),
            new Product(2, "Bread", 12.5));

        assertEquals(expectedProducts, productRepository.getProducts());
    }

    @Test
    void can_add_product_without_id_after_added_with_id_less_than_max() {
        doNothing().when(productValidator).validate(any());
        productRepository.addProduct(new Product(2, "Milk2", 45.6));
        productRepository.addProduct(new Product(1, "Milk1", 45.6));
        productRepository.addProduct(new Product("Bread", 12.5));

        List<Product> expectedProducts = List.of(new Product(2, "Milk2", 45.6),
            new Product(1, "Milk1", 45.6),
            new Product(3, "Bread", 12.5));

        assertEquals(expectedProducts, productRepository.getProducts());
    }

    @Test
    void can_get_product() throws Exception {
        Product expectedProduct = new Product(1, "Milk", 45.6);
        List<Product> products = List.of(expectedProduct);
        Field field = ProductRepositoryImpl.class.getDeclaredField("products");
        field.setAccessible(true);
        field.set(productRepository, products);

        assertEquals(expectedProduct, productRepository.getProductById(1));
    }

    @Test
    void can_get_null_by_not_existing_id() {
        assertNull(productRepository.getProductById(1));
    }

    @Test
    void can_delete_product_by_id() throws Exception {
        Product product = new Product(1, "Milk", 45.6);
        List<Product> products = new ArrayList<>(List.of(product));
        Field field = ProductRepositoryImpl.class.getDeclaredField("products");
        field.setAccessible(true);
        field.set(productRepository, products);

        boolean successfulDeleted = productRepository.deleteProductById(1);

        assertTrue(successfulDeleted);
        assertFalse(productRepository.getProducts().contains(product));
    }

    @Test
    void can_not_delete_product_by_not_existing_id() {
        boolean successfulDeleted = productRepository.deleteProductById(1);

        assertFalse(successfulDeleted);
    }

    @Test
    void can_update_product_by_id() throws Exception {
        List<Product> products = List.of(new Product(1, "Milk", 45.6));
        Field field = ProductRepositoryImpl.class.getDeclaredField("products");
        field.setAccessible(true);
        field.set(productRepository, products);

        Product updatedProduct = new Product("MilkyWay", 25.6);
        boolean successfulUpdated = productRepository.updateProductById(updatedProduct, 1);

        Product expectedProduct = new Product(1, "MilkyWay", 25.6);

        assertEquals(expectedProduct, productRepository.getProductById(1));
        assertTrue(successfulUpdated);
    }

    @Test
    void can_not_update_product_by_id_to_not_valid_product() {
        ProductRepository productRepository = spy(this.productRepository);
        when(productRepository.getProductById(anyInt())).thenReturn(new Product(1, "Milk", 45.6));
        doThrow(new IllegalArgumentException()).when(productValidator).validate(any());

        assertThrows(
            IllegalArgumentException.class,
            () -> productRepository.updateProductById(null, 1)
        );
    }

    @Test
    void can_not_update_product_by_not_existing_id() {
        Product updatedProduct = new Product("MilkyWay", 25.6);
        boolean successfulUpdated = productRepository.updateProductById(updatedProduct, 1);

        assertFalse(successfulUpdated);
    }
}
