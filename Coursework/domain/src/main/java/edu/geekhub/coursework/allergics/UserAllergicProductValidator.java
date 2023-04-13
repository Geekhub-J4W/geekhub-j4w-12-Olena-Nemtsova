package edu.geekhub.coursework.allergics;

import edu.geekhub.coursework.products.interfaces.ProductRepository;
import edu.geekhub.coursework.users.interfaces.UserRepository;
import org.springframework.stereotype.Component;

@Component
public class UserAllergicProductValidator {
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public UserAllergicProductValidator(UserRepository userRepository,
                                        ProductRepository productRepository) {
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    public void validate(UserAllergicProduct userAllergicProduct) {
        if (userAllergicProduct == null) {
            throw new IllegalArgumentException("UserAllergicProduct was null");
        }
        validateUserId(userAllergicProduct.getUserId());
        validateProductId(userAllergicProduct.getProductId());
    }

    private void validateUserId(int userId) {
        if (userRepository.getUserById(userId) == null) {
            throw new IllegalArgumentException("UserAllergicProduct had not exists user id");
        }
    }

    private void validateProductId(int productId) {
        if (productRepository.getProductById(productId) == null) {
            throw new IllegalArgumentException("UserAllergicProduct had not exists product id");
        }
    }
}
