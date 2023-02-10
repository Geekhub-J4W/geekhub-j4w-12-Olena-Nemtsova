package edu.geekhub.homework.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
    private ProductService productService;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private ProductValidator productValidator;

    @BeforeEach
    void setUp() throws Exception {
        productService = new ProductService();
        Field field = ProductService.class.getDeclaredField("productRepository");
        field.setAccessible(true);
        field.set(productService, productRepository);
        field = ProductService.class.getDeclaredField("productValidator");
        field.setAccessible(true);
        field.set(productService, productValidator);
    }

    @Test
    void can_get_products() {
        List<Product> expectedProducts = List.of(new Product("Milk", 45.6));
        when(productRepository.getProducts()).thenReturn(expectedProducts);

        List<Product> products = productService.getProducts();

        assertEquals(expectedProducts, products);
    }

    @Test
    void can_get_sorted_by_name_products() {
        List<Product> products = List.of(new Product("Milk", 45.6),
            new Product("Bread", 12.5));
        when(productRepository.getProducts()).thenReturn(products);

        List<Product> sortedProducts = productService.getSortedByNameProducts();

        List<Product> expectedSortedProducts = List.of(new Product("Bread", 12.5),
            new Product("Milk", 45.6));

        assertEquals(expectedSortedProducts, sortedProducts);
    }

    @Test
    void can_get_sorted_by_price_products() {
        List<Product> products = List.of(new Product("Milk", 45.6),
            new Product("Bread", 12.5));
        when(productRepository.getProducts()).thenReturn(products);

        List<Product> sortedProducts = productService.getSortedByPriceProducts();

        List<Product> expectedSortedProducts = List.of(new Product("Bread", 12.5),
            new Product("Milk", 45.6));

        assertEquals(expectedSortedProducts, sortedProducts);
    }

    @Test
    void can_add_product() {
        doNothing().when(productValidator).validate(any());
        doNothing().when(productRepository).addProduct(any());

        boolean successfulAdded = productService.addProduct(null);

        assertTrue(successfulAdded);
    }

    @Test
    void can_not_add_not_valid_product() {
        doThrow(new IllegalArgumentException()).when(productValidator).validate(any());

        boolean successfulAdded = productService.addProduct(null);

        assertFalse(successfulAdded);
    }

    @Test
    void can_delete_product_by_id() {
        when(productRepository.deleteProductById(anyInt())).thenReturn(true);

        boolean successfulDeleted = productService.deleteProductById(1);

        assertTrue(successfulDeleted);
    }

    @Test
    void can_not_delete_product_by_not_existing_id() {
        when(productRepository.deleteProductById(anyInt())).thenReturn(false);

        boolean successfulDeleted = productService.deleteProductById(1);

        assertFalse(successfulDeleted);
    }

    @Test
    void can_update_product_by_id() {
        doNothing().when(productValidator).validate(any());
        when(productRepository.updateProductById(any(), anyInt())).thenReturn(true);

        boolean successfulUpdated = productService.updateProductById(null, 1);

        assertTrue(successfulUpdated);
    }

    @Test
    void can_not_update_product_by_id_to_not_valid_product() {
        doThrow(new IllegalArgumentException()).when(productValidator).validate(any());

        boolean successfulUpdated = productService.updateProductById(null, 1);

        assertFalse(successfulUpdated);
    }

    @Test
    void can_not_update_product_by_not_existing_id() {
        doNothing().when(productValidator).validate(any());
        when(productRepository.updateProductById(any(), anyInt())).thenReturn(false);

        boolean successfulUpdated = productService.updateProductById(null, 1);

        assertFalse(successfulUpdated);
    }
}
