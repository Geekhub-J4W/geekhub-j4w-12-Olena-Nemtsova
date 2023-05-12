package edu.geekhub.coursework.dishes;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;

import edu.geekhub.coursework.TestApplication;
import edu.geekhub.coursework.config.DatabaseContainer;
import edu.geekhub.coursework.config.DatabaseTestConfig;
import edu.geekhub.coursework.products.Product;
import edu.geekhub.coursework.products.ProductRepositoryImpl;
import edu.geekhub.coursework.productsdishes.ProductDishRepositoryImpl;
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
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@DataJdbcTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(classes = {DatabaseTestConfig.class, TestApplication.class})
@Testcontainers
class DishRepositoryImplTest {
    @ClassRule
    @Container
    public static PostgreSQLContainer<DatabaseContainer> postgresSQLContainer =
        DatabaseContainer.getInstance();
    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;
    @Mock
    private DishValidator validator;
    private DishRepositoryImpl dishRepository;

    @BeforeEach
    void setUp() {
        dishRepository = new DishRepositoryImpl(validator, jdbcTemplate);
    }

    @Test
    void can_get_dishes() {
        List<Dish> dishes = dishRepository.getDishes();

        Assertions.assertEquals(20, dishes.size());
    }

    @Test
    void can_get_dish_by_id() {
        Dish expectedDish = new Dish(1, "Milk rice porridge", null);

        Dish dish = dishRepository.getDishById(1);

        assertEquals(expectedDish, dish);
    }

    @Test
    void can_get_null_dish_by_wrong_id() {
        Dish dish = dishRepository.getDishById(-1);

        assertNull(dish);
    }

    @Test
    void can_add_dish() {
        Dish expectedDish = new Dish(-1, "Some dish", null);

        int newDishId = dishRepository.addDish(expectedDish);

        expectedDish.setId(newDishId);
        assertEquals(
            expectedDish,
            dishRepository.getDishById(newDishId)
        );
    }

    @Test
    void can_not_add_dish_with_not_unique_name() {
        Dish dish = new Dish(-1, "Borsch", null);

        assertThrows(
            DuplicateKeyException.class,
            () -> dishRepository.addDish(dish)
        );
    }

    @Test
    void can_not_add_not_valid_dish() {
        doThrow(new IllegalArgumentException()).when(validator).validate(any());

        assertThrows(
            IllegalArgumentException.class,
            () -> dishRepository.addDish(null)
        );
    }

    @Test
    void can_delete_dish_by_id() {
        dishRepository.deleteDishById(1);

        assertNull(dishRepository.getDishById(1));
    }

    @Test
    void can_update_dish_by_id() {
        Dish expectedDish = new Dish(1, "Some dish", null);

        dishRepository.updateDishById(expectedDish, 1);

        assertEquals(expectedDish, dishRepository.getDishById(1));
    }

    @Test
    void can_not_update_dish_by_id_to_not_valid() {
        doThrow(new IllegalArgumentException()).when(validator).validate(any());

        assertThrows(
            IllegalArgumentException.class,
            () -> dishRepository.updateDishById(null, 1)
        );
    }

    @Test
    void can_get_dish_calories() {
        ProductRepositoryImpl productRepository =
            new ProductRepositoryImpl(jdbcTemplate, null);
        ProductDishRepositoryImpl productDishRepository =
            new ProductDishRepositoryImpl(null, jdbcTemplate);

        int expectedCalories = productRepository
            .getProductsByDishId(1).stream()
            .mapToInt(
                product ->
                    product.getCalories()
                    * productDishRepository.getRelationByProductAndDishId(product.getId(), 1)
                        .getProductQuantity()
                    / 100
            ).sum();

        int calories = dishRepository.getDishCalories(1);

        assertEquals(expectedCalories, calories);
    }

    @Test
    void can_get_zero_dish_calories_by_wrong_id() {
        int calories = dishRepository.getDishCalories(-1);

        assertEquals(0, calories);
    }

    @Test
    void can_get_dish_weight() {
        ProductRepositoryImpl productRepository =
            new ProductRepositoryImpl(jdbcTemplate, null);
        ProductDishRepositoryImpl productDishRepository =
            new ProductDishRepositoryImpl(null, jdbcTemplate);

        int expectedWeight = productRepository
            .getProductsByDishId(1).stream()
            .mapToInt(
                product ->
                    productDishRepository.getRelationByProductAndDishId(product.getId(), 1)
                        .getProductQuantity()
            ).sum();

        int weight = dishRepository.getDishWeight(1);

        assertEquals(expectedWeight, weight);
    }

    @Test
    void can_get_zero_dish_weight_by_wrong_id() {
        int weight = dishRepository.getDishWeight(-1);

        assertEquals(0, weight);
    }

    @Test
    void can_get_dishes_name_sorted_by_page_and_input() {
        List<Dish> expectedDishes = new ArrayList<>(List.of(
            new Dish(1, "Milk rice porridge", null)
        ));

        List<Dish> dishes = dishRepository.getDishesNameSortedByPageAndInput(1, 2, "m");

        assertEquals(expectedDishes, dishes);
    }

    @Test
    void can_get_empty_dishes_name_sorted_by_wrong_input() {
        List<Dish> dishes = dishRepository.getDishesNameSortedByPageAndInput(
            1,
            2,
            "some not existing dish"
        );

        assertEquals(new ArrayList<>(), dishes);
    }

    @Test
    void can_get_dishes_by_calories_range_not_contains_user_allergic_products() {
        ProductRepositoryImpl productRepository = new ProductRepositoryImpl(jdbcTemplate, null);
        List<Product> allergic = productRepository.getProductsByUserId(3);

        List<Dish> dishes = dishRepository.getDishesByCaloriesRangeAndUserId(350, 500, 3);

        assertFalse(
            dishes.stream().anyMatch(dish ->
                productRepository.getProductsByDishId(dish.getId())
                    .stream().anyMatch(allergic::contains)
            ));
        assertTrue(dishes.stream().allMatch(
            dish -> dishRepository.getDishCalories(dish.getId()) >= 350
                    && dishRepository.getDishCalories(dish.getId()) <= 500
        ));
    }
}
