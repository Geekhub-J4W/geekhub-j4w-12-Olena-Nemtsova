package edu.geekhub.coursework.products;

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

import edu.geekhub.coursework.dishes.Dish;
import edu.geekhub.coursework.dishes.interfaces.DishRepository;
import edu.geekhub.coursework.products.interfaces.ProductRepository;
import edu.geekhub.coursework.users.User;
import edu.geekhub.coursework.users.interfaces.UserRepository;
import edu.geekhub.coursework.util.PageValidator;
import java.util.ArrayList;
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
    private ProductValidator validator;
    @Mock
    private ProductRepository repository;
    @Mock
    private PageValidator pageValidator;
    @Mock
    private DishRepository dishRepository;
    @Mock
    private UserRepository userRepository;
    private Product product;

    @BeforeEach
    void setUp() {
        productService = new ProductServiceImpl(
            repository,
            validator,
            pageValidator,
            dishRepository,
            userRepository
        );

        product = new Product(1, "Rice", 130);
    }

    @Test
    void can_get_product_by_id() {
        doReturn(product).when(repository).getProductById(anyInt());

        Product gotProduct = productService.getProductById(1);

        assertEquals(product, gotProduct);
    }

    @Test
    void can_add_product() {
        doNothing().when(validator).validate(any());
        doReturn(1).when(repository).addProduct(any());
        doReturn(product).when(repository).getProductById(anyInt());

        Product addedProduct = productService.addProduct(product);

        assertNotNull(addedProduct);
    }

    @Test
    void can_not_add_not_valid_product() {
        doThrow(new IllegalArgumentException()).when(validator).validate(any());

        Product newProduct = productService.addProduct(null);

        assertNull(newProduct);
    }

    @Test
    void can_not_add_product_not_retrieved_generated_key_from_repository() {
        doNothing().when(validator).validate(any());
        doReturn(-1).when(repository).addProduct(any());

        Product addedProduct = productService.addProduct(null);

        assertNull(addedProduct);
    }

    @Test
    void can_not_add_product_not_added_at_repository() {
        doNothing().when(validator).validate(any());
        doThrow(new DataAccessException("") {
        }).when(repository).addProduct(any());

        Product addedProduct = productService.addProduct(product);

        assertNull(addedProduct);
    }

    @Test
    void can_delete_product_by_id() {
        doReturn(product).when(repository).getProductById(anyInt());
        doNothing().when(repository).deleteProductById(anyInt());

        boolean successfulDeleted = productService.deleteProductById(1);

        assertTrue(successfulDeleted);
    }

    @Test
    void can_not_delete_product_by_not_existing_id() {
        doReturn(null).when(repository).getProductById(anyInt());

        boolean successfulDeleted = productService.deleteProductById(0);

        assertFalse(successfulDeleted);
    }

    @Test
    void can_not_delete_product_not_deleted_at_repository() {
        doReturn(product).when(repository).getProductById(anyInt());
        doThrow(new DataAccessException("") {
        }).when(repository).deleteProductById(anyInt());

        boolean successfulDeleted = productService.deleteProductById(1);

        assertFalse(successfulDeleted);
    }

    @Test
    void can_update_product_by_id() {
        doNothing().when(validator).validate(any());
        doReturn(product).when(repository).getProductById(anyInt());
        doNothing().when(repository).updateProductById(any(), anyInt());

        Product updatedProduct = productService.updateProductById(product, 1);

        assertNotNull(updatedProduct);
    }

    @Test
    void can_not_update_product_to_not_valid_product() {
        doReturn(product).when(repository).getProductById(anyInt());
        doThrow(new IllegalArgumentException()).when(validator).validate(any());

        Product updatedProduct = productService.updateProductById(null, 1);

        assertNull(updatedProduct);
    }

    @Test
    void can_not_update_product_by_not_existing_id() {
        doReturn(null).when(repository).getProductById(anyInt());

        Product updatedProduct = productService.updateProductById(product, 1);

        assertNull(updatedProduct);
    }

    @Test
    void can_not_update_product_by_id_not_updated_at_repository() {
        doNothing().when(validator).validate(any());
        doReturn(product).when(repository).getProductById(anyInt());
        doThrow(new DataAccessException("") {
        })
            .when(repository).updateProductById(any(), anyInt());

        Product updatedProduct = productService.updateProductById(product, 1);

        assertNull(updatedProduct);
    }

    @Test
    void can_get_products() {
        doReturn(List.of(product)).when(repository).getProducts();

        List<Product> products = productService.getProducts();

        assertEquals(List.of(product), products);
    }

    @Test
    void can_get_count_of_products_pages_by_limit_and_input() {
        doNothing().when(pageValidator).validatePageLimit(anyInt());
        productService = spy(productService);
        doReturn(List.of(product, product)).when(productService).getProducts();

        int countOfPages = productService.getCountOfPages(1, "ric");

        assertEquals(2, countOfPages);
    }

    @Test
    void can_get_one_products_pages_by_wrong_limit() {
        doThrow(new IllegalArgumentException()).when(pageValidator).validatePageLimit(anyInt());

        int countOfPages = productService.getCountOfPages(0, "ric");

        assertEquals(1, countOfPages);
    }

    @Test
    void can_get_one_products_pages_by_wrong_input() {
        doNothing().when(pageValidator).validatePageLimit(anyInt());
        productService = spy(productService);
        doReturn(List.of(product, product)).when(productService).getProducts();

        int countOfPages = productService.getCountOfPages(0, "some product search");

        assertEquals(1, countOfPages);
    }

    @Test
    void can_get_products_name_sorted_by_page_and_input() {
        doNothing().when(pageValidator).validatePageLimit(anyInt());
        doNothing().when(pageValidator).validatePageNumber(anyInt(), anyInt());
        doReturn(List.of(product))
            .when(repository).getProductsNameSortedByPageAndInput(anyInt(), anyInt(), any());

        List<Product> products = productService.getProductsNameSortedByPageAndInput(
            1,
            1,
            "some product search"
        );

        assertEquals(List.of(product), products);
    }

    @Test
    void can_get_empty_products_by_page_and_wrong_limit() {
        doThrow(new IllegalArgumentException()).when(pageValidator).validatePageLimit(anyInt());

        List<Product> products = productService.getProductsNameSortedByPageAndInput(
            -1,
            1,
            "some product search"
        );

        assertEquals(new ArrayList<>(), products);
    }

    @Test
    void can_get_empty_products_by_limit_and_wrong_page() {
        doNothing().when(pageValidator).validatePageLimit(anyInt());
        doThrow(new IllegalArgumentException())
            .when(pageValidator).validatePageNumber(anyInt(), anyInt());

        List<Product> products = productService.getProductsNameSortedByPageAndInput(
            1,
            -1,
            "some product search"
        );

        assertEquals(new ArrayList<>(), products);
    }

    @Test
    void can_get_products_of_dish() {
        doReturn(new Dish()).when(dishRepository).getDishById(anyInt());
        doReturn(List.of(product)).when(repository).getProductsByDishId(anyInt());

        List<Product> products = productService.getProductsOfDish(1);

        assertEquals(List.of(product), products);
    }

    @Test
    void can_get_empty_products_of_not_existing_dish() {
        doReturn(null).when(dishRepository).getDishById(anyInt());

        List<Product> products = productService.getProductsOfDish(1);

        assertEquals(new ArrayList<>(), products);
    }

    @Test
    void can_get_user_allergic_products() {
        doReturn(new User()).when(userRepository).getUserById(anyInt());
        doReturn(List.of(product)).when(repository).getProductsByUserId(anyInt());

        List<Product> products = productService.getUserAllergicProducts(1);

        assertEquals(List.of(product), products);
    }

    @Test
    void can_get_empty_allergic_products_of_not_existing_user() {
        doReturn(null).when(userRepository).getUserById(anyInt());

        List<Product> products = productService.getUserAllergicProducts(1);

        assertEquals(new ArrayList<>(), products);
    }
}
