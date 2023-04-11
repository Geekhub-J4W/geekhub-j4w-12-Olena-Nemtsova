package edu.geekhub.homework;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import edu.geekhub.homework.products.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class ProductConsoleParserTest {
    private ProductConsoleParser productConsoleParser;

    @BeforeEach
    void setUp() {
        productConsoleParser = new ProductConsoleParser();
    }

    @Test
    void can_get_product_from_console_input() {
        String consoleInput = "Milk, 45.90, 1";
        Product product = productConsoleParser.parse(consoleInput);

        Product expectedProduct = new Product("Milk", 45.90, 1);

        assertEquals(expectedProduct, product);
    }

    @ParameterizedTest
    @ValueSource(strings = {"Milk,", "Milk 45.90, 1", "Milk, forty-five"})
    void can_get_null_product_from_wrong_console_input(String consoleInput) {
        Product product = productConsoleParser.parse(consoleInput);

        assertNull(product);
    }
}
