package edu.geekhub.coursework.productsdishes;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;

import edu.geekhub.coursework.TestApplication;
import edu.geekhub.coursework.config.DatabaseContainer;
import edu.geekhub.coursework.config.DatabaseTestConfig;
import org.junit.ClassRule;
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
class ProductDishRepositoryImplTest {
    @ClassRule
    @Container
    public static PostgreSQLContainer<DatabaseContainer> postgresSQLContainer =
        DatabaseContainer.getInstance();
    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;
    @Mock
    private ProductDishValidator validator;
    private ProductDishRepositoryImpl productDishRepository;

    @BeforeEach
    void setUp() {
        productDishRepository = new ProductDishRepositoryImpl(validator, jdbcTemplate);
    }

    @Test
    void can_get_product_dish_relation() {
        ProductDish expectedRelation = new ProductDish(1, 1, 80);

        ProductDish relation = productDishRepository.getRelationByProductAndDishId(1, 1);

        assertEquals(expectedRelation, relation);
    }

    @Test
    void can_get_null_product_dish_relation_by_wrong_ids() {
        ProductDish relation = productDishRepository.getRelationByProductAndDishId(-1, -1);

        assertNull(relation);
    }

    @Test
    void can_add_product_dish_relation() {
        ProductDish expectedRelation = new ProductDish(2, 1, 80);

        productDishRepository.addRelation(expectedRelation);

        assertEquals(
            expectedRelation,
            productDishRepository.getRelationByProductAndDishId(2, 1)
        );
    }

    @Test
    void can_not_add_not_valid_product_dish_relation() {
        doThrow(new IllegalArgumentException()).when(validator).validate(any());

        assertThrows(
            IllegalArgumentException.class,
            () -> productDishRepository.addRelation(null)
        );
    }

    @Test
    void can_delete_product_dish_relation() {
        productDishRepository.deleteRelationByProductAndDishId(1, 1);

        assertNull(
            productDishRepository.getRelationByProductAndDishId(1, 1)
        );
    }

    @Test
    void can_update_product_dish_relation() {
        ProductDish expectedRelation = new ProductDish(1, 1, 100);

        productDishRepository.updateRelationByProductAndDishId(expectedRelation);

        assertEquals(
            expectedRelation,
            productDishRepository.getRelationByProductAndDishId(1, 1)
        );
    }

    @Test
    void can_not_update_product_dish_relation_to_not_valid() {
        doThrow(new IllegalArgumentException()).when(validator).validate(any());

        assertThrows(
            IllegalArgumentException.class,
            () -> productDishRepository.updateRelationByProductAndDishId(null)
        );
    }
}
