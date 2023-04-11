package edu.geekhub.homework.favorites.interfaces;

import edu.geekhub.homework.favorites.Favorite;
import edu.geekhub.homework.products.Product;
import java.util.List;

public interface FavoriteRepository {
    int addFavorite(Favorite favorite);

    Favorite getFavoriteByProductAndUserId(int productId, int userId);

    void deleteFavoriteByProductAndUserId(int productId, int userId);

    List<Product> getFavoriteUserProducts(int userId);
}
