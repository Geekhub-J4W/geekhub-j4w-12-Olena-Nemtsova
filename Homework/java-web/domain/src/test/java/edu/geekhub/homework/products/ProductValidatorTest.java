package edu.geekhub.homework.products;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

import edu.geekhub.homework.categories.Category;
import edu.geekhub.homework.categories.interfaces.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ProductValidatorTest {
    private ProductValidator productValidator;
    @Mock
    private CategoryRepository categoryRepository;

    private Product product;

    @BeforeEach
    void setUp() {
        productValidator = new ProductValidator(categoryRepository);
        product = spy(
            new Product(1, "Milk", 49.5, 1, null, 1)
        );
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
        "p, Product name had wrong length"
    })
    void can_not_validate_product_with_wrong_name(String name, String expectedMessage) {
        doReturn(name).when(product).getName();

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
        "1.456, Product price had too much numbers after point"
    })
    void can_not_validate_product_with_wrong_price(Double price, String expectedMessage) {
        doReturn(price).when(product).getPrice();

        IllegalArgumentException thrown = assertThrows(
            IllegalArgumentException.class,
            () -> productValidator.validate(product)
        );

        assertEquals(expectedMessage, thrown.getMessage());
    }

    @Test
    void can_not_validate_product_with_not_exists_category() {
        doReturn(null).when(categoryRepository).getCategoryById(anyInt());

        IllegalArgumentException thrown = assertThrows(
            IllegalArgumentException.class,
            () -> productValidator.validate(product)
        );

        assertEquals("Product had not exists category id", thrown.getMessage());
    }

    @Test
    void can_not_validate_product_with_quantity_less_than_zero() {
        doReturn(new Category()).when(categoryRepository).getCategoryById(anyInt());
        doReturn(-1).when(product).getQuantity();

        IllegalArgumentException thrown = assertThrows(
            IllegalArgumentException.class,
            () -> productValidator.validate(product)
        );

        assertEquals("Product quantity was less than zero", thrown.getMessage());
    }

    @Test
    void can_validate_correct_product() {
        doReturn(new Category()).when(categoryRepository).getCategoryById(anyInt());

        assertDoesNotThrow(
            () -> productValidator.validate(product)
        );
    }
}
