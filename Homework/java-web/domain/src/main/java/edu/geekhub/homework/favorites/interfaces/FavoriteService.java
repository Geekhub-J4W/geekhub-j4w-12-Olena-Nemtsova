package edu.geekhub.homework.favorites.interfaces;

import edu.geekhub.homework.favorites.Favorite;
import edu.geekhub.homework.products.Product;
import java.util.List;

public interface FavoriteService {
    Favorite addFavorite(Favorite favorite);

    boolean favoriteExits(int productId, int userId);

    boolean deleteFavoriteByProductAndUserId(int productId, int userId);

    List<Product> getFavoriteUserProducts(int userId);
}
