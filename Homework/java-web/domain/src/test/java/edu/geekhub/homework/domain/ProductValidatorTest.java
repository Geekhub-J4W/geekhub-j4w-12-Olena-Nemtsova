package edu.geekhub.homework.domain;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doReturn;

import edu.geekhub.homework.repository.interfaces.CategoryRepository;
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
    @Mock
    private Product product;

    @BeforeEach
    void setUp() {
        productValidator = new ProductValidator(categoryRepository);
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
        "1.456, Product price had too much numbers after point",
    })
    void can_not_validate_product_with_wrong_price(Double price, String expectedMessage) {
        doReturn("Milk").when(product).getName();
        doReturn(price).when(product).getPrice();

        IllegalArgumentException thrown = assertThrows(
            IllegalArgumentException.class,
            () -> productValidator.validate(product)
        );

        assertEquals(expectedMessage, thrown.getMessage());
    }

    @Test
    void can_not_validate_product_with_not_exists_category() {
        doReturn("Milk").when(product).getName();
        doReturn(49.5).when(product).getPrice();
        doReturn(1).when(product).getCategoryId();
        doReturn(null).when(categoryRepository).getCategoryById(anyInt());

        IllegalArgumentException thrown = assertThrows(
            IllegalArgumentException.class,
            () -> productValidator.validate(product)
        );

        assertEquals("Product had not exists category id", thrown.getMessage());
    }

    @Test
    void can_not_validate_product_with_quantity_less_than_zero() {
        doReturn("Milk").when(product).getName();
        doReturn(49.5).when(product).getPrice();
        doReturn(1).when(product).getCategoryId();
        doReturn(-1).when(product).getQuantity();
        doReturn(new Category(1, "Dairy")).when(categoryRepository).getCategoryById(anyInt());

        IllegalArgumentException thrown = assertThrows(
            IllegalArgumentException.class,
            () -> productValidator.validate(product)
        );

        assertEquals("Product quantity was less than zero", thrown.getMessage());
    }

    @Test
    void can_validate_correct_product() {
        Product product = new Product("Milk", 1.67, 1);
        product.setQuantity(1);
        doReturn(new Category(1, "Dairy")).when(categoryRepository).getCategoryById(anyInt());

        assertDoesNotThrow(
            () -> productValidator.validate(product)
        );
    }
}
