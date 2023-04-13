package edu.geekhub.coursework.allergics;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doReturn;

import edu.geekhub.coursework.products.Product;
import edu.geekhub.coursework.products.interfaces.ProductRepository;
import edu.geekhub.coursework.users.User;
import edu.geekhub.coursework.users.interfaces.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserAllergicProductValidatorTest {
    private UserAllergicProductValidator validator;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ProductRepository productRepository;
    private UserAllergicProduct userAllergicProduct;

    @BeforeEach
    void setUp() {
        validator = new UserAllergicProductValidator(userRepository, productRepository);

        userAllergicProduct = new UserAllergicProduct(1, 1);
    }

    @Test
    void can_not_validate_null_userAllergicProduct() {
        IllegalArgumentException thrown = assertThrows(
            IllegalArgumentException.class,
            () -> validator.validate(null)
        );

        assertEquals("UserAllergicProduct was null", thrown.getMessage());
    }

    @Test
    void can_not_validate_userAllergicProduct_with_wrong_userId() {
        doReturn(null).when(userRepository).getUserById(anyInt());

        IllegalArgumentException thrown = assertThrows(
            IllegalArgumentException.class,
            () -> validator.validate(userAllergicProduct)
        );

        assertEquals("UserAllergicProduct had not exists user id", thrown.getMessage());
    }

    @Test
    void can_not_validate_userAllergicProduct_with_wrong_productId() {
        doReturn(new User()).when(userRepository).getUserById(anyInt());
        doReturn(null).when(productRepository).getProductById(anyInt());

        IllegalArgumentException thrown = assertThrows(
            IllegalArgumentException.class,
            () -> validator.validate(userAllergicProduct)
        );

        assertEquals("UserAllergicProduct had not exists product id", thrown.getMessage());
    }

    @Test
    void can_validate_correct_userAllergicProduct() {
        doReturn(new User()).when(userRepository).getUserById(anyInt());
        doReturn(new Product()).when(productRepository).getProductById(anyInt());

        assertDoesNotThrow(
            () -> validator.validate(userAllergicProduct)
        );
    }
}
