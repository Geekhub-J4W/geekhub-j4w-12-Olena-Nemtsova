package edu.geekhub.homework.favorites;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doReturn;

import edu.geekhub.homework.products.Product;
import edu.geekhub.homework.products.interfaces.ProductRepository;
import edu.geekhub.homework.users.User;
import edu.geekhub.homework.users.interfaces.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class FavoriteValidatorTest {
    private FavoriteValidator favoriteValidator;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private UserRepository userRepository;
    private Favorite favorite;

    @BeforeEach
    void setUp() {
        favoriteValidator = new FavoriteValidator(productRepository, userRepository);
        favorite = new Favorite(1, 1, 1);
    }

    @Test
    void can_not_validate_null_favorite() {
        IllegalArgumentException thrown = assertThrows(
            IllegalArgumentException.class,
            () -> favoriteValidator.validate(null)
        );

        assertEquals("Favorite user product was null", thrown.getMessage());
    }

    @Test
    void can_not_validate_favorite_with_not_exists_product() {
        doReturn(new User()).when(userRepository).getUserById(anyInt());
        doReturn(null).when(productRepository).getProductById(anyInt());

        IllegalArgumentException thrown = assertThrows(
            IllegalArgumentException.class,
            () -> favoriteValidator.validate(favorite)
        );

        assertEquals("Product with id '1' doesn't exist", thrown.getMessage());
    }


    @Test
    void can_not_validate_favorite_with_not_exists_user() {
        doReturn(null).when(userRepository).getUserById(anyInt());

        IllegalArgumentException thrown = assertThrows(
            IllegalArgumentException.class,
            () -> favoriteValidator.validate(favorite)
        );

        assertEquals("User with id '1' doesn't exist", thrown.getMessage());
    }

    @Test
    void can_validate_correct_productOrder() {
        doReturn(new Product()).when(productRepository).getProductById(anyInt());
        doReturn(new User()).when(userRepository).getUserById(anyInt());

        assertDoesNotThrow(
            () -> favoriteValidator.validate(favorite)
        );
    }
}
