package edu.geekhub.homework.products;

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

import edu.geekhub.homework.categories.Category;
import edu.geekhub.homework.categories.interfaces.CategoryService;
import edu.geekhub.homework.products.interfaces.ProductRepository;
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
    private Product product;

    @BeforeEach
    void setUp() {
        productService = new ProductServiceImpl(productRepository,
            productValidator,
            categoryService);

        product = new Product(1, "Milk", 49.5, 1, null, 1);
    }

    @Test
    void can_get_products() {
        List<Product> expectedProducts = List.of(product);
        doReturn(expectedProducts).when(productRepository).getProducts();

        List<Product> products = productService.getProducts();

        assertEquals(expectedProducts, products);
    }

    @Test
    void can_get_product_by_id() {
        doReturn(product).when(productRepository).getProductById(anyInt());

        Product gotProduct = productService.getProductById(1);

        assertEquals(product, gotProduct);
    }

    @Test
    void can_get_null_product_by_wrong_id() {
        doReturn(null).when(productRepository).getProductById(anyInt());

        Product gotProduct = productService.getProductById(0);

        assertNull(gotProduct);
    }

    @Test
    void can_add_product() {
        doNothing().when(productValidator).validate(any());
        doReturn(1).when(productRepository).addProduct(any());
        doReturn(product).when(productRepository).getProductById(anyInt());
        Product addedProduct = productService.addProduct(product);

        assertNotNull(addedProduct);
    }

    @Test
    void can_not_add_product_not_get_product_id_from_repository() {
        doNothing().when(productValidator).validate(any());
        doReturn(-1).when(productRepository).addProduct(any());

        Product addedProduct = productService.addProduct(null);

        assertNull(addedProduct);
    }

    @Test
    void can_not_add_product_not_added_at_repository() {
        doNothing().when(productValidator).validate(any());
        doThrow(new DataAccessException("") {
        }).when(productRepository).addProduct(any());

        Product addedProduct = productService.addProduct(product);

        assertNull(addedProduct);
    }

    @Test
    void can_not_add_not_valid_product() {
        doThrow(new IllegalArgumentException()).when(productValidator).validate(any());

        Product newProduct = productService.addProduct(null);

        assertNull(newProduct);
    }

    @Test
    void can_delete_product_by_id() {
        doReturn(product).when(productRepository).getProductById(anyInt());
        doNothing().when(productRepository).deleteProductById(anyInt());

        boolean successfulDeleted = productService.deleteProductById(1);

        assertTrue(successfulDeleted);
    }

    @Test
    void can_not_delete_product_by_not_existing_id() {
        doReturn(null).when(productRepository).getProductById(anyInt());

        boolean successfulDeleted = productService.deleteProductById(0);

        assertFalse(successfulDeleted);
    }

    @Test
    void can_not_delete_product_not_deleted_at_repository() {
        doReturn(product).when(productRepository).getProductById(anyInt());
        doThrow(new DataAccessException("") {
        }).when(productRepository).deleteProductById(anyInt());

        boolean successfulDeleted = productService.deleteProductById(1);

        assertFalse(successfulDeleted);
    }

    @Test
    void can_update_product_by_id() {
        doNothing().when(productValidator).validate(any());
        doReturn(product).when(productRepository).getProductById(anyInt());
        doNothing().when(productRepository).updateProductById(any(), anyInt());

        Product updatedProduct = productService.updateProductById(product, 1);

        assertNotNull(updatedProduct);
    }

    @Test
    void can_not_update_product_to_not_valid_product() {
        doThrow(new IllegalArgumentException()).when(productValidator).validate(any());

        Product updatedProduct = productService.updateProductById(null, 1);

        assertNull(updatedProduct);
    }

    @Test
    void can_not_update_product_by_not_existing_id() {
        doNothing().when(productValidator).validate(any());
        doReturn(null).when(productRepository).getProductById(anyInt());

        Product updatedProduct = productService.updateProductById(product, 1);

        assertNull(updatedProduct);
    }

    @Test
    void can_not_update_product_not_updated_at_repository() {
        doNothing().when(productValidator).validate(any());
        doReturn(product).when(productRepository).getProductById(anyInt());
        doThrow(new DataAccessException("") {
        })
            .when(productRepository).updateProductById(any(), anyInt());

        Product updatedProduct = productService.updateProductById(product, 1);

        assertNull(updatedProduct);
    }


    @Test
    void can_get_sorted_products_by_category_with_pagination() {
        List<Product> products = List.of(product);
        doReturn(new Category()).when(categoryService).getCategoryById(anyInt());
        doReturn(products).when(productRepository)
            .getProductsOrdersSortedWithPagination(anyInt(), anyInt(), any());


        List<Product> sortedProducts = productService.getSortedProductsByCategoryWithPagination(
            ProductsSortType.ORDERS,
            1,
            1,
            1);

        assertEquals(products, sortedProducts);
    }

    @Test
    void can_get_sorted_products_by_category_with_default_page_limit() {
        List<Product> products = List.of(product);
        doReturn(new Category()).when(categoryService).getCategoryById(anyInt());
        doReturn(products).when(productRepository)
            .getProductsNameSortedWithPagination(anyInt(), anyInt(), any());


        List<Product> sortedProducts = productService.getSortedProductsByCategoryWithPagination(
            ProductsSortType.NAME,
            1,
            -1,
            1);

        assertEquals(products, sortedProducts);
    }

    @Test
    void can_get_all_sorted_products_by_wrong_category_with_pagination() {
        List<Product> products = List.of(product);
        doReturn(null).when(categoryService).getCategoryById(anyInt());
        doReturn(products).when(productRepository)
            .getProductsPriceSortedWithPagination(anyInt(), anyInt(), any());

        List<Product> sortedProducts = productService.getSortedProductsByCategoryWithPagination(
            ProductsSortType.PRICE,
            -1,
            1,
            1);

        assertEquals(products, sortedProducts);
    }

    @Test
    void can_get_rating_sorted_products_by_null_sortType_with_pagination() {
        List<Product> products = List.of(product);
        doReturn(new Category()).when(categoryService).getCategoryById(anyInt());
        doReturn(products).when(productRepository)
            .getProductsRatingSortedWithPagination(anyInt(), anyInt(), any());

        List<Product> sortedProducts = productService.getSortedProductsByCategoryWithPagination(
            null,
            1,
            1,
            1);

        assertEquals(products, sortedProducts);
    }

    @Test
    void can_get_count_of_products_pages_by_category_and_page_limit() {
        List<Product> products = List.of(product,
            new Product("Bread", 12.5, 2));
        doReturn(new Category()).when(categoryService).getCategoryById(anyInt());
        productService = spy(this.productService);
        doReturn(products).when(productService).getProducts();

        int countOfPages = productService.getCountOfPages(1, 1);

        assertEquals(1, countOfPages);
    }

    @Test
    void can_get_count_of_all_products_pages_by_wrong_category() {
        List<Product> products = List.of(product,
            new Product("Ice cream", 22.5, 1));
        doReturn(null).when(categoryService).getCategoryById(anyInt());
        productService = spy(this.productService);
        doReturn(products).when(productService).getProducts();

        int countOfPages = productService.getCountOfPages(1, 1);

        assertEquals(2, countOfPages);
    }

    @Test
    void can_get_count_of_products_pages_by_wrong_limit() {
        List<Product> products = List.of(product, product);
        doReturn(new Category()).when(categoryService).getCategoryById(anyInt());
        productService = spy(this.productService);
        doReturn(products).when(productService).getProducts();

        int countOfPages = productService.getCountOfPages(1, -1);

        assertEquals(2, countOfPages);
    }

    @Test
    void can_get_products_search_by_name() {
        List<Product> products = List.of(product,
            new Product("Bread", 12.5, 2));
        productService = spy(this.productService);
        doReturn(products).when(productService).getProducts();

        List<Product> gotProducts = productService.getProductsNameContainsInput("mil");

        assertEquals(List.of(product), gotProducts);
    }
}
