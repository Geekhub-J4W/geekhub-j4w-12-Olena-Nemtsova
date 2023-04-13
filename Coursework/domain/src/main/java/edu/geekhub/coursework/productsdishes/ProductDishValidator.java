package edu.geekhub.coursework.productsdishes;

import edu.geekhub.coursework.dishes.interfaces.DishRepository;
import edu.geekhub.coursework.products.interfaces.ProductRepository;
import org.springframework.stereotype.Component;

@Component
public class ProductDishValidator {
    private final ProductRepository productRepository;
    private final DishRepository dishRepository;

    public ProductDishValidator(
        ProductRepository productRepository,
        DishRepository dishRepository
    ) {
        this.productRepository = productRepository;
        this.dishRepository = dishRepository;
    }

    public void validate(ProductDish productDish) {
        if (productDish == null) {
            throw new IllegalArgumentException("ProductDish relation was null");
        }
        validateProductQuantity(productDish.getProductQuantity());
        validateProductId(productDish.getProductId());
        validateDishId(productDish.getDishId());
    }

    private void validateDishId(int dishId) {
        if (dishRepository.getDishById(dishId) == null) {
            throw new IllegalArgumentException("ProductDish relation had not exists dish id");
        }
    }

    private void validateProductId(int productId) {
        if (productRepository.getProductById(productId) == null) {
            throw new IllegalArgumentException("ProductDish relation had not exists product id");
        }
    }

    private void validateProductQuantity(int productQuantity) {
        if (productQuantity <= 0) {
            throw new IllegalArgumentException(
                "ProductDish relation had productQuantity equals or less than zero"
            );
        }
    }
}
