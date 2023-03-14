package edu.geekhub.homework.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import edu.geekhub.homework.domain.Category;
import edu.geekhub.homework.domain.Product;
import edu.geekhub.homework.domain.ProductValidator;
import edu.geekhub.homework.domain.ProductsSortType;
import edu.geekhub.homework.repository.interfaces.ProductRepository;
import edu.geekhub.homework.service.interfaces.CategoryService;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessException;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {
    private ProductServiceImpl productService;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private ProductValidator productValidator;
    @Mock
    private CategoryService categoryService;

    @BeforeEach
    void setUp() {
        productService = new ProductServiceImpl(productRepository,
            productValidator,
            categoryService);
    }

    @Test
    void can_get_products() {
        List<Product> expectedProducts = List.of(new Product("Milk", 45.6, 1));
        when(productRepository.getProducts()).thenReturn(expectedProducts);

        List<Product> products = productService.getProducts();

        assertEquals(expectedProducts, products);
    }

    @Test
    void can_get_product_by_id() {
        Product milk = new Product(1, "Milk", 45.6, 1, null);
        when(productRepository.getProducts()).thenReturn(List.of(milk));

        Product product = productService.getProductById(1);

        assertEquals(milk, product);
    }

    @Test
    void can_get_null_product_by_wrong_id() {
        Product milk = new Product(1, "Milk", 45.6, 1, null);
        when(productRepository.getProducts()).thenReturn(List.of(milk));

        Product product = productService.getProductById(0);

        assertNull(product);
    }

    @Test
    void can_get_sorted_by_name_products() {
        List<Product> products = List.of(new Product("Milk", 45.6, 1),
            new Product("Bread", 12.5, 1));
        when(productRepository.getProducts()).thenReturn(products);

        List<Product> sortedProducts = productService.getSortedByNameProducts();

        List<Product> expectedSortedProducts = List.of(new Product("Bread", 12.5, 1),
            new Product("Milk", 45.6, 1));

        assertEquals(expectedSortedProducts, sortedProducts);
    }

    @Test
    void can_get_sorted_by_price_products() {
        List<Product> products = List.of(new Product("Milk", 45.6, 1),
            new Product("Bread", 12.5, 1));
        when(productRepository.getProducts()).thenReturn(products);

        List<Product> sortedProducts = productService.getSortedByPriceProducts();

        List<Product> expectedSortedProducts = List.of(new Product("Bread", 12.5, 1),
            new Product("Milk", 45.6, 1));

        assertEquals(expectedSortedProducts, sortedProducts);
    }

    @Test
    void can_get_sorted_products_by_category() {
        List<Product> products = List.of(new Product("Milk", 45.6, 1),
            new Product("Bread", 12.5, 2));
        when(productRepository.getProductsRatingSorted()).thenReturn(products);
        when(categoryService.getCategoryById(anyInt())).thenReturn(new Category("Daily"));

        List<Product> sortedProducts = productService.getSortedProducts(ProductsSortType.RATING, 1);

        List<Product> expectedSortedProducts = List.of(new Product("Milk", 45.6, 1));

        assertEquals(expectedSortedProducts, sortedProducts);
    }

    @Test
    void can_get_all_products_sorted_by_not_exists_category() {
        List<Product> products = List.of(new Product("Milk", 45.6, 1),
            new Product("Bread", 12.5, 2));
        productService = spy(this.productService);
        when(productService.getProductsRatingSorted()).thenReturn(products);
        when(categoryService.getCategoryById(anyInt())).thenReturn(null);

        List<Product> sortedProducts = productService.getSortedProducts(
            ProductsSortType.RATING,
            -1
        );

        assertEquals(products, sortedProducts);
    }

    @Test
    void can_add_product() {
        doNothing().when(productValidator).validate(any());
        doReturn(1).when(productRepository).addProduct(any());
        Product milk = new Product("Milk", 45.6, 1);
        productService = spy(this.productService);
        doReturn(milk).when(productService).getProductById(anyInt());
        Product newProduct = productService.addProduct(milk);

        assertNotNull(newProduct);
    }

    @Test
    void can_not_add_product_not_added_to_repository() {
        doNothing().when(productValidator).validate(any());
        doReturn(-1).when(productRepository).addProduct(any());

        Product newProduct = productService.addProduct(null);

        assertNull(newProduct);
    }

    @Test
    void can_not_add_not_valid_product() {
        doThrow(new IllegalArgumentException()).when(productValidator).validate(any());

        Product newProduct = productService.addProduct(null);

        assertNull(newProduct);
    }

    @Test
    void can_delete_product_by_id() {
        Product milk = new Product("Milk", 45.6, 1);
        productService = spy(this.productService);
        doReturn(milk).when(productService).getProductById(anyInt());
        doNothing().when(productRepository).deleteProductById(anyInt());

        boolean successfulDeleted = productService.deleteProductById(1);

        assertTrue(successfulDeleted);
    }

    @Test
    void can_not_delete_product_by_not_existing_id() {
        productService = spy(this.productService);
        doReturn(null).when(productService).getProductById(anyInt());

        boolean successfulDeleted = productService.deleteProductById(1);

        assertFalse(successfulDeleted);
    }

    @Test
    void can_not_delete_product_not_deleted_at_repository() {
        Product milk = new Product("Milk", 45.6, 1);
        productService = spy(this.productService);
        doReturn(milk).when(productService).getProductById(anyInt());
        doThrow(new DataAccessException("") {
        }).when(productRepository).deleteProductById(anyInt());

        boolean successfulDeleted = productService.deleteProductById(1);

        assertFalse(successfulDeleted);
    }

    @Test
    void can_update_product_by_id() {
        productService = spy(this.productService);
        doNothing().when(productValidator).validate(any());
        doReturn(new Product("Milk", 45.6, 1)).when(productService).getProductById(anyInt());
        doNothing().when(productRepository).updateProductById(any(), anyInt());

        Product updatedProduct = productService.updateProductById(null, 1);

        assertNotNull(updatedProduct);
    }

    @Test
    void can_not_update_product_by_id_to_not_valid_product() {
        doThrow(new IllegalArgumentException()).when(productValidator).validate(any());

        Product updatedProduct = productService.updateProductById(null, 1);

        assertNull(updatedProduct);
    }

    @Test
    void can_not_update_product_by_not_existing_id() {
        productService = spy(this.productService);
        doNothing().when(productValidator).validate(any());
        doReturn(null).when(productService).getProductById(anyInt());

        Product updatedProduct = productService.updateProductById(null, 1);

        assertNull(updatedProduct);
    }

    @Test
    void can_not_update_product_not_updated_at_repository() {
        productService = spy(this.productService);
        doNothing().when(productValidator).validate(any());
        doReturn(new Product("Milk", 45.6, 1)).when(productService).getProductById(anyInt());
        doThrow(new DataAccessException("") {
        })
            .when(productRepository).updateProductById(any(), anyInt());

        Product updatedProduct = productService.updateProductById(null, 1);

        assertNull(updatedProduct);
    }
}
