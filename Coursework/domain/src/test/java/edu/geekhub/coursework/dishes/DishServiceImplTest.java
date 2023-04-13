package edu.geekhub.coursework.dishes;

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

import edu.geekhub.coursework.dishes.interfaces.DishRepository;
import edu.geekhub.coursework.usersparameters.interfaces.UserParametersService;
import edu.geekhub.coursework.util.PageValidator;
import edu.geekhub.coursework.util.TypeOfMeal;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessException;

@ExtendWith(MockitoExtension.class)
class DishServiceImplTest {
    private DishServiceImpl dishService;
    @Mock
    private DishValidator validator;
    @Mock
    private PageValidator pageValidator;
    @Mock
    private DishRepository repository;
    @Mock
    private UserParametersService userParametersService;
    private Dish dish;

    @BeforeEach
    void setUp() {
        dishService = new DishServiceImpl(
            validator,
            pageValidator,
            repository,
            userParametersService
        );

        dish = new Dish(1, "Borsch", null);
    }

    @Test
    void can_get_dish_by_id() {
        doReturn(dish).when(repository).getDishById(anyInt());

        Dish gotDish = dishService.getDishById(1);

        assertNotNull(gotDish);
    }

    @Test
    void can_add_dish() {
        doNothing().when(validator).validate(any());
        doReturn(1).when(repository).addDish(any());
        doReturn(dish).when(repository).getDishById(anyInt());

        Dish addedDish = dishService.addDish(dish);

        assertNotNull(addedDish);
    }

    @Test
    void can_not_add_not_valid_dish() {
        doThrow(new IllegalArgumentException()).when(validator).validate(any());

        Dish addedDish = dishService.addDish(dish);

        assertNull(addedDish);
    }

    @Test
    void can_not_add_dish_not_added_at_repository() {
        doNothing().when(validator).validate(any());
        doThrow(new DataAccessException("") {
        }).when(repository).addDish(any());

        Dish addedDish = dishService.addDish(dish);

        assertNull(addedDish);
    }

    @Test
    void can_not_add_dish_not_retrieved_generated_key() {
        doNothing().when(validator).validate(any());
        doReturn(-1).when(repository).addDish(any());

        Dish addedDish = dishService.addDish(dish);

        assertNull(addedDish);
    }

    @Test
    void can_delete_dish_by_id() {
        doReturn(dish).when(repository).getDishById(anyInt());
        doNothing().when(repository).deleteDishById(anyInt());

        boolean successfulDeleted = dishService.deleteDishById(1);

        assertTrue(successfulDeleted);
    }

    @Test
    void can_not_delete_dish_by_wrong_id() {
        doReturn(null).when(repository).getDishById(anyInt());

        boolean successfulDeleted = dishService.deleteDishById(1);

        assertFalse(successfulDeleted);
    }

    @Test
    void can_not_delete_dish_not_deleted_at_repository() {
        doReturn(dish).when(repository).getDishById(anyInt());
        doThrow(new DataAccessException("") {
        }).when(repository).deleteDishById(anyInt());

        boolean successfulDeleted = dishService.deleteDishById(1);

        assertFalse(successfulDeleted);
    }

    @Test
    void can_update_dish_by_id() {
        doReturn(dish).when(repository).getDishById(anyInt());
        doNothing().when(validator).validate(any());
        doNothing().when(repository).updateDishById(any(), anyInt());

        Dish updatedDish = dishService.updateDishById(dish, 1);

        assertNotNull(updatedDish);
    }

    @Test
    void can_not_update_dish_by_wrong_id() {
        doReturn(null).when(repository).getDishById(anyInt());

        Dish updatedDish = dishService.updateDishById(dish, 1);

        assertNull(updatedDish);
    }

    @Test
    void can_not_update_dish_by_id_to_wrong_dish() {
        doReturn(dish).when(repository).getDishById(anyInt());
        doThrow(new IllegalArgumentException()).when(validator).validate(any());

        Dish updatedDish = dishService.updateDishById(dish, 1);

        assertNull(updatedDish);
    }

    @Test
    void can_not_update_dish_by_id_not_updated_at_repository() {
        doReturn(dish).when(repository).getDishById(anyInt());
        doNothing().when(validator).validate(any());
        doThrow(new DataAccessException("") {
        }).when(repository).updateDishById(any(), anyInt());

        Dish updatedDish = dishService.updateDishById(dish, 1);

        assertNull(updatedDish);
    }

    @Test
    void can_get_dishes() {
        doReturn(List.of(dish)).when(repository).getDishes();

        List<Dish> dishes = dishService.getDishes();

        assertEquals(List.of(dish), dishes);
    }

    @Test
    void can_get_dishes_by_userId_and_type_of_meal() {
        doReturn(500).when(userParametersService).getUserCaloriesByTypeOfMeal(anyInt(), any());
        doReturn(List.of(dish))
            .when(repository).getDishesByCaloriesRangeAndUserId(anyInt(), anyInt(), anyInt());

        List<Dish> dishes = dishService.getDishesByUserIdAndTypeOfMeal(1, TypeOfMeal.DINNER);

        assertEquals(List.of(dish), dishes);
    }

    @Test
    void can_get_empty_dishes_list_by_wrong_userId_or_type_of_meal() {
        doReturn(0).when(userParametersService).getUserCaloriesByTypeOfMeal(anyInt(), any());

        List<Dish> dishes = dishService.getDishesByUserIdAndTypeOfMeal(-1, null);

        assertEquals(new ArrayList<>(), dishes);
    }

    @Test
    void can_get_count_of_dishes_pages_by_limit_and_input() {
        doNothing().when(pageValidator).validatePageLimit(anyInt());
        dishService = spy(dishService);
        doReturn(List.of(dish, dish)).when(dishService).getDishes();

        int countOfPages = dishService.getCountOfPages(1, "bors");

        assertEquals(2, countOfPages);
    }

    @Test
    void can_get_one_dishes_pages_by_wrong_limit() {
        doThrow(new IllegalArgumentException()).when(pageValidator).validatePageLimit(anyInt());

        int countOfPages = dishService.getCountOfPages(0, "bors");

        assertEquals(1, countOfPages);
    }

    @Test
    void can_get_one_dishes_pages_by_wrong_input() {
        doNothing().when(pageValidator).validatePageLimit(anyInt());
        dishService = spy(dishService);
        doReturn(List.of(dish, dish)).when(dishService).getDishes();

        int countOfPages = dishService.getCountOfPages(0, "some dish search");

        assertEquals(1, countOfPages);
    }

    @Test
    void can_get_dishes_name_sorted_by_page_and_input() {
        doNothing().when(pageValidator).validatePageLimit(anyInt());
        doNothing().when(pageValidator).validatePageNumber(anyInt(), anyInt());
        doReturn(List.of(dish))
            .when(repository).getDishesNameSortedByPageAndInput(anyInt(), anyInt(), any());

        List<Dish> dishes = dishService.getDishesNameSortedByPageAndInput(
            1,
            1,
            "some dish search"
        );

        assertEquals(List.of(dish), dishes);
    }

    @Test
    void can_get_empty_dishes_by_page_and_wrong_limit() {
        doThrow(new IllegalArgumentException()).when(pageValidator).validatePageLimit(anyInt());

        List<Dish> dishes = dishService.getDishesNameSortedByPageAndInput(
            -1,
            1,
            "some dish search"
        );

        assertEquals(new ArrayList<>(), dishes);
    }

    @Test
    void can_get_empty_dishes_by_limit_and_wrong_page() {
        doNothing().when(pageValidator).validatePageLimit(anyInt());
        doThrow(new IllegalArgumentException())
            .when(pageValidator).validatePageNumber(anyInt(), anyInt());

        List<Dish> dishes = dishService.getDishesNameSortedByPageAndInput(
            1,
            -1,
            "some dish search"
        );

        assertEquals(new ArrayList<>(), dishes);
    }
}
