package edu.geekhub.homework.domain;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class ProductValidatorTest {
    private ProductValidator productValidator;

    @BeforeEach
    void setUp() {
        productValidator = new ProductValidator();
    }

    @Test
    void can_not_validate_null_product() {
        IllegalArgumentException thrown = assertThrows(
            IllegalArgumentException.class,
            () -> productValidator.validate(null)
        );

        assertEquals("Product was null", thrown.getMessage());
    }

    @ParameterizedTest
    @CsvSource({
        ", Product name was null",
        "'', Product name was empty",
        "p, Product name had wrong length",
    })
    void can_not_validate_product_with_wrong_name(String name, String expectedMessage) {
        Product product = new Product(name, 0);

        IllegalArgumentException thrown = assertThrows(
            IllegalArgumentException.class,
            () -> productValidator.validate(product)
        );

        assertEquals(expectedMessage, thrown.getMessage());
    }

    @ParameterizedTest
    @CsvSource({
        "0, Product price was equals or less than zero",
        "-1, Product price was equals or less than zero",
        "1.456, Product price had too much numbers after point",
    })
    void can_not_validate_product_with_wrong_price(Double price, String expectedMessage) {
        Product product = new Product("Milk", price);

        IllegalArgumentException thrown = assertThrows(
            IllegalArgumentException.class,
            () -> productValidator.validate(product)
        );

        assertEquals(expectedMessage, thrown.getMessage());
    }

    @Test
    void can_validate_correct_product() {
        Product product = new Product("Milk", 1.67);
        assertDoesNotThrow(
            () -> productValidator.validate(product)
        );
    }
}
