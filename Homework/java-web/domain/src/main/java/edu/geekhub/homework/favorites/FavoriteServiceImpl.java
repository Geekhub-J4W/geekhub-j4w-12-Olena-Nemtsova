package edu.geekhub.homework.favorites;

import edu.geekhub.homework.favorites.interfaces.FavoriteRepository;
import edu.geekhub.homework.favorites.interfaces.FavoriteService;
import edu.geekhub.homework.products.Product;
import java.util.List;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.tinylog.Logger;

@Service
public class FavoriteServiceImpl implements FavoriteService {
    private final FavoriteValidator validator;
    private final FavoriteRepository favoriteRepository;

    public FavoriteServiceImpl(FavoriteValidator validator,
                               FavoriteRepository favoriteRepository) {
        this.validator = validator;
        this.favoriteRepository = favoriteRepository;
    }

    @Override
    public Favorite addFavorite(Favorite favorite) {
        try {
            validator.validate(favorite);
            int id = favoriteRepository.addFavorite(favorite);
            if (id == -1) {
                throw new IllegalArgumentException("Unable to retrieve the generated key");
            }
            favorite.setId(id);

            Logger.info("Favorite user product was added:\n" + favorite);
            return favoriteRepository.getFavoriteByProductAndUserId(
                favorite.getProductId(),
                favorite.getUserId()
            );
        } catch (IllegalArgumentException | DataAccessException exception) {
            Logger.warn("Favorite wasn't added: " + favorite + "\n" + exception.getMessage());
            return null;
        }
    }

    @Override
    public boolean favoriteExits(int productId, int userId) {
        return favoriteRepository.getFavoriteByProductAndUserId(productId, userId) != null;
    }

    @Override
    public boolean deleteFavoriteByProductAndUserId(int productId, int userId) {
        Favorite favoriteToDel = favoriteRepository
            .getFavoriteByProductAndUserId(productId, userId);
        try {
            if (favoriteToDel == null) {
                throw new IllegalArgumentException("Favorite with productId '" + productId
                                                   + "' and userId '" + userId
                                                   + "' not found");
            }
            favoriteRepository.deleteFavoriteByProductAndUserId(productId, userId);
            Logger.info("Favorite was deleted:\n" + favoriteToDel);
            return true;
        } catch (IllegalArgumentException | DataAccessException exception) {
            Logger.warn("Favorite wasn't deleted: "
                        + favoriteToDel + "\n"
                        + exception.getMessage());
            return false;
        }
    }

    @Override
    public List<Product> getFavoriteUserProducts(int userId) {
        return favoriteRepository.getFavoriteUserProducts(userId);
    }
}
