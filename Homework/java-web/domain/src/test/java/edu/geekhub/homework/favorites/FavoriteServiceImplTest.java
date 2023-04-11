package edu.geekhub.homework.favorites;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

import edu.geekhub.homework.favorites.interfaces.FavoriteRepository;
import edu.geekhub.homework.products.Product;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessException;

@ExtendWith(MockitoExtension.class)
class FavoriteServiceImplTest {
    private FavoriteServiceImpl favoriteService;
    @Mock
    private FavoriteValidator favoriteValidator;
    @Mock
    private FavoriteRepository favoriteRepository;
    private Favorite favorite;

    @BeforeEach
    void setUp() {
        favoriteService = new FavoriteServiceImpl(favoriteValidator, favoriteRepository);
        favorite = new Favorite(1, 1, 1);
    }

    @Test
    void can_add_favorite() {
        doNothing().when(favoriteValidator).validate(any());
        doReturn(1).when(favoriteRepository).addFavorite(any());
        doReturn(favorite)
            .when(favoriteRepository).getFavoriteByProductAndUserId(anyInt(), anyInt());

        Favorite addedFavorite = favoriteService.addFavorite(favorite);

        assertNotNull(addedFavorite);
    }

    @Test
    void can_not_add_favorite_not_get_favorite_id_from_repository() {
        doNothing().when(favoriteValidator).validate(any());
        doReturn(-1).when(favoriteRepository).addFavorite(any());

        Favorite addedFavorite = favoriteService.addFavorite(favorite);

        assertNull(addedFavorite);
    }

    @Test
    void can_not_add_favorite_not_added_at_repository() {
        doNothing().when(favoriteValidator).validate(any());
        doThrow(new DataAccessException("") {
        }).when(favoriteRepository).addFavorite(any());

        Favorite addedFavorite = favoriteService.addFavorite(favorite);

        assertNull(addedFavorite);
    }

    @Test
    void can_not_add_not_valid_favorite() {
        doThrow(new IllegalArgumentException()).when(favoriteValidator).validate(any());

        Favorite addedFavorite = favoriteService.addFavorite(null);

        assertNull(addedFavorite);
    }

    @Test
    void can_check_is_favorite_exists() {
        doReturn(favorite).when(favoriteRepository).getFavoriteByProductAndUserId(1, 1);
        doReturn(null).when(favoriteRepository).getFavoriteByProductAndUserId(-1, -1);

        boolean favoriteExists1 = favoriteService.favoriteExits(1, 1);
        boolean favoriteExists2 = favoriteService.favoriteExits(-1, -1);

        assertTrue(favoriteExists1);
        assertFalse(favoriteExists2);
    }

    @Test
    void can_delete_favorite_by_product_and_user_id() {
        doReturn(favorite)
            .when(favoriteRepository).getFavoriteByProductAndUserId(anyInt(), anyInt());
        doNothing().when(favoriteRepository).deleteFavoriteByProductAndUserId(anyInt(), anyInt());

        boolean successfulDeleted = favoriteService.deleteFavoriteByProductAndUserId(1, 1);

        assertTrue(successfulDeleted);
    }

    @Test
    void can_not_delete_not_exists_favorite() {
        doReturn(null).when(favoriteRepository).getFavoriteByProductAndUserId(anyInt(), anyInt());

        boolean successfulDeleted = favoriteService.deleteFavoriteByProductAndUserId(1, 1);

        assertFalse(successfulDeleted);
    }

    @Test
    void can_not_delete_favorite_not_deleted_at_repository() {
        doReturn(favorite)
            .when(favoriteRepository).getFavoriteByProductAndUserId(anyInt(), anyInt());
        doThrow(new DataAccessException("") {
        }).when(favoriteRepository).deleteFavoriteByProductAndUserId(anyInt(), anyInt());

        boolean successfulDeleted = favoriteService.deleteFavoriteByProductAndUserId(1, 1);

        assertFalse(successfulDeleted);
    }

    @Test
    void can_get_favorite_user_products_by_user_id() {
        Product product = new Product(1, "Milk", 49.5, 1, null, 1);
        doReturn(List.of(product, product))
            .when(favoriteRepository).getFavoriteUserProducts(anyInt());

        List<Product> products = favoriteService.getFavoriteUserProducts(1);

        assertEquals(List.of(product, product), products);
    }
}
