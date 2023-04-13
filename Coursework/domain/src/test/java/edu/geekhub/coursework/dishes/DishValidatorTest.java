package edu.geekhub.coursework.dishes;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DishValidatorTest {
    private DishValidator validator;
    private Dish dish;

    @BeforeEach
    void setUp() {
        validator = new DishValidator();

        dish = spy(
            new Dish(1, "Borsch", null)
        );
    }

    @Test
    void can_not_validate_null_dish() {
        IllegalArgumentException thrown = assertThrows(
            IllegalArgumentException.class,
            () -> validator.validate(null)
        );

        assertEquals("Dish was null", thrown.getMessage());
    }

    @ParameterizedTest
    @CsvSource({
        ", Dish name was null",
        "'', Dish name was empty",
        "d, Dish name had wrong length"
    })
    void can_not_validate_dish_with_wrong_name(String name, String expectedMessage) {
        doReturn(name).when(dish).getName();

        IllegalArgumentException thrown = assertThrows(
            IllegalArgumentException.class,
            () -> validator.validate(dish)
        );

        assertEquals(expectedMessage, thrown.getMessage());
    }

    @Test
    void can_validate_correct_dish() {
        assertDoesNotThrow(
            () -> validator.validate(dish)
        );
    }
}
