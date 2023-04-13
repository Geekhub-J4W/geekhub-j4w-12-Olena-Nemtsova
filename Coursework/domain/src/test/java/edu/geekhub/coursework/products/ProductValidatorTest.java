package edu.geekhub.coursework.products;

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
class ProductValidatorTest {
    private ProductValidator validator;
    private Product product;

    @BeforeEach
    void setUp() {
        validator = new ProductValidator();

        product = spy(
            new Product(1, "Rice", 130)
        );
    }

    @Test
    void can_not_validate_null_product() {
        IllegalArgumentException thrown = assertThrows(
            IllegalArgumentException.class,
            () -> validator.validate(null)
        );

        assertEquals("Product was null", thrown.getMessage());
    }

    @ParameterizedTest
    @CsvSource({
        ", Product name was null",
        "'', Product name was empty",
        "p, Product name had wrong length"
    })
    void can_not_validate_product_with_wrong_name(String name, String expectedMessage) {
        doReturn(name).when(product).getName();

        IllegalArgumentException thrown = assertThrows(
            IllegalArgumentException.class,
            () -> validator.validate(product)
        );

        assertEquals(expectedMessage, thrown.getMessage());
    }

    @Test
    void can_not_validate_product_with_calories_less_than_zero() {
        doReturn(-1).when(product).getCalories();

        IllegalArgumentException thrown = assertThrows(
            IllegalArgumentException.class,
            () -> validator.validate(product)
        );

        assertEquals("Product calories was less than zero", thrown.getMessage());
    }

    @Test
    void can_validate_correct_product() {
        assertDoesNotThrow(
            () -> validator.validate(product)
        );
    }
}
