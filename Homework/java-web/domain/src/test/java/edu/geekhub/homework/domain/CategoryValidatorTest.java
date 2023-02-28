package edu.geekhub.homework.domain;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class CategoryValidatorTest {
    private CategoryValidator categoryValidator;

    @BeforeEach
    void setUp() {
        categoryValidator = new CategoryValidator();
    }

    @Test
    void can_not_validate_null_category() {
        IllegalArgumentException thrown = assertThrows(
            IllegalArgumentException.class,
            () -> categoryValidator.validate(null)
        );

        assertEquals("Category was null", thrown.getMessage());
    }

    @ParameterizedTest
    @CsvSource({
        ", Category name was null",
        "'', Category name was empty",
        "c, Category name had wrong length",
    })
    void can_not_validate_product_with_wrong_name(String name, String expectedMessage) {
        Category category = new Category(name);

        IllegalArgumentException thrown = assertThrows(
            IllegalArgumentException.class,
            () -> categoryValidator.validate(category)
        );

        assertEquals(expectedMessage, thrown.getMessage());
    }

    @Test
    void can_validate_correct_category() {
        Category category = new Category("Dairy");
        assertDoesNotThrow(
            () -> categoryValidator.validate(category)
        );
    }
}
