package edu.geekhub.homework.favorites;

import edu.geekhub.homework.products.Product;
import edu.geekhub.homework.products.interfaces.ProductRepository;
import edu.geekhub.homework.users.interfaces.UserRepository;
import org.springframework.stereotype.Component;

@Component
public class FavoriteValidator {
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public FavoriteValidator(ProductRepository productRepository, UserRepository userRepository) {
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    public void validate(Favorite favorite) {
        if (favorite == null) {
            throw new IllegalArgumentException("Favorite user product was null");
        }
        validateUserExists(favorite.getUserId());
        validateProductExists(favorite.getProductId());
    }

    private void validateProductExists(int productId) {
        Product product = productRepository.getProductById(productId);
        if (product == null) {
            throw new IllegalArgumentException("Product with id '" + productId + "' doesn't exist");
        }
    }

    private void validateUserExists(int userId) {
        if (userRepository.getUserById(userId) == null) {
            throw new IllegalArgumentException("User with id '" + userId + "' doesn't exist");
        }
    }
}
