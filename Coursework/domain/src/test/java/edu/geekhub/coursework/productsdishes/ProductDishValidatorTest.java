package edu.geekhub.coursework.productsdishes;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

import edu.geekhub.coursework.dishes.Dish;
import edu.geekhub.coursework.dishes.interfaces.DishRepository;
import edu.geekhub.coursework.products.Product;
import edu.geekhub.coursework.products.interfaces.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ProductDishValidatorTest {
    private ProductDishValidator validator;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private DishRepository dishRepository;
    private ProductDish productDish;

    @BeforeEach
    void setUp() {
        validator = new ProductDishValidator(productRepository, dishRepository);

        productDish = spy(
            new ProductDish(1, 1, 60)
        );
    }

    @Test
    void can_not_validate_null_productDish() {
        IllegalArgumentException thrown = assertThrows(
            IllegalArgumentException.class,
            () -> validator.validate(null)
        );

        assertEquals("ProductDish relation was null", thrown.getMessage());
    }

    @ParameterizedTest
    @CsvSource({
        "0, ProductDish relation had productQuantity equals or less than zero",
        "-1, ProductDish relation had productQuantity equals or less than zero"
    })
    void can_not_validate_productDish_with_wrong_productQuantity(
        int quantity,
        String expectedMessage
    ) {
        doReturn(quantity).when(productDish).getProductQuantity();

        IllegalArgumentException thrown = assertThrows(
            IllegalArgumentException.class,
            () -> validator.validate(productDish)
        );

        assertEquals(expectedMessage, thrown.getMessage());
    }

    @Test
    void can_not_validate_productDish_with_wrong_productId() {
        doReturn(null).when(productRepository).getProductById(anyInt());

        IllegalArgumentException thrown = assertThrows(
            IllegalArgumentException.class,
            () -> validator.validate(productDish)
        );

        assertEquals("ProductDish relation had not exists product id", thrown.getMessage());
    }

    @Test
    void can_not_validate_productDish_with_wrong_dishId() {
        doReturn(new Product()).when(productRepository).getProductById(anyInt());
        doReturn(null).when(dishRepository).getDishById(anyInt());

        IllegalArgumentException thrown = assertThrows(
            IllegalArgumentException.class,
            () -> validator.validate(productDish)
        );

        assertEquals("ProductDish relation had not exists dish id", thrown.getMessage());
    }

    @Test
    void can_validate_correct_productDish() {
        doReturn(new Product()).when(productRepository).getProductById(anyInt());
        doReturn(new Dish()).when(dishRepository).getDishById(anyInt());

        assertDoesNotThrow(
            () -> validator.validate(productDish)
        );
    }
}
