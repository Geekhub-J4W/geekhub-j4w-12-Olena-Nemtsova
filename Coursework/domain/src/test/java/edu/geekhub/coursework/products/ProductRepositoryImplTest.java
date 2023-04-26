package edu.geekhub.coursework.products;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;

import edu.geekhub.coursework.TestApplication;
import edu.geekhub.coursework.config.DatabaseContainer;
import edu.geekhub.coursework.config.DatabaseTestConfig;
import java.util.ArrayList;
import java.util.List;
import org.junit.ClassRule;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@DataJdbcTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(classes = {DatabaseTestConfig.class, TestApplication.class})
@Testcontainers
class ProductRepositoryImplTest {
    @ClassRule
    @Container
    public static PostgreSQLContainer<DatabaseContainer> postgresSQLContainer =
        DatabaseContainer.getInstance();
    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;
    @Mock
    private ProductValidator validator;
    private ProductRepositoryImpl productRepository;

    @BeforeEach
    void setUp() {
        productRepository = new ProductRepositoryImpl(jdbcTemplate, validator);
    }

    @Test
    void can_get_products() {
        List<Product> products = productRepository.getProducts();

        Assertions.assertEquals(49, products.size());
    }

    @Test
    void can_get_product_by_id() {
        Product expectedProduct = new Product(1, "Rice", 130);

        Product product = productRepository.getProductById(1);

        assertEquals(expectedProduct, product);
    }

    @Test
    void can_get_null_product_by_wrong_id() {
        Product product = productRepository.getProductById(-1);

        assertNull(product);
    }

    @Test
    void can_add_product() {
        Product product = new Product(-1, "Some product", 20);

        int newProductId = productRepository.addProduct(product);

        product.setId(50);
        assertEquals(product, productRepository.getProductById(newProductId));
    }

    @Test
    void can_not_add_not_valid_product() {
        doThrow(new IllegalArgumentException()).when(validator).validate(any());

        assertThrows(
            IllegalArgumentException.class,
            () -> productRepository.addProduct(null)
        );
    }

    @Test
    void can_delete_product_by_id() {
        productRepository.deleteProductById(1);

        assertNull(productRepository.getProductById(1));
    }

    @Test
    void can_update_product_by_id() {
        Product expectedProduct = new Product(1, "Some product", 20);

        productRepository.updateProductById(expectedProduct, 1);

        assertEquals(expectedProduct, productRepository.getProductById(1));
    }

    @Test
    void can_not_update_product_by_id_to_not_valid() {
        doThrow(new IllegalArgumentException()).when(validator).validate(any());

        assertThrows(
            IllegalArgumentException.class,
            () -> productRepository.updateProductById(null, 1)
        );
    }

    @Test
    void can_get_products_of_dish_by_dish_id() {
        List<Product> expectedProducts = new ArrayList<>(List.of(
            new Product(1, "Rice", 130),
            new Product(5, "Milk", 64),
            new Product(31, "Butter", 717),
            new Product(39, "Water", 0),
            new Product(40, "Sugar", 387)
        ));

        List<Product> products = productRepository.getProductsByDishId(1);

        assertEquals(expectedProducts, products);
    }

    @Test
    void can_get_empty_products_of_dish_by_wrong_dish_id() {
        List<Product> products = productRepository.getProductsByDishId(-1);

        assertEquals(new ArrayList<>(), products);
    }

    @Test
    void can_get_user_allergic_products_by_user_id() {
        List<Product> expectedProducts = new ArrayList<>(List.of(
            new Product(40, "Sugar", 387)
        ));

        List<Product> products = productRepository.getProductsByUserId(3);

        assertEquals(expectedProducts, products);
    }

    @Test
    void can_get_empty_user_allergic_products_by_wrong_user_id() {
        List<Product> products = productRepository.getProductsByUserId(-1);

        assertEquals(new ArrayList<>(), products);
    }

    @Test
    void can_get_products_name_sorted_by_page_and_input() {
        List<Product> expectedProducts = new ArrayList<>(List.of(
            new Product(29, "Avocado", 195)
        ));

        List<Product> products = productRepository.getProductsNameSortedByPageAndInput(1, 2, "a");

        assertEquals(expectedProducts, products);
    }

    @Test
    void can_get_empty_products_name_sorted_by_wrong_input() {
        List<Product> products = productRepository.getProductsNameSortedByPageAndInput(
            1,
            2,
            "some not existing product"
        );

        assertEquals(new ArrayList<>(), products);
    }
}
