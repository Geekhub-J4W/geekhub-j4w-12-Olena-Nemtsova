package edu.geekhub.coursework.products;

import edu.geekhub.coursework.dishes.interfaces.DishRepository;
import edu.geekhub.coursework.products.interfaces.ProductRepository;
import edu.geekhub.coursework.products.interfaces.ProductService;
import edu.geekhub.coursework.users.interfaces.UserRepository;
import edu.geekhub.coursework.util.PageValidator;
import java.util.ArrayList;
import java.util.List;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.tinylog.Logger;

@Service
public class ProductServiceImpl implements ProductService {
    private final ProductValidator validator;
    private final PageValidator pageValidator;
    private final ProductRepository productRepository;
    private final DishRepository dishRepository;
    private final UserRepository userRepository;

    public ProductServiceImpl(ProductRepository productRepository,
                              ProductValidator validator,
                              PageValidator pageValidator,
                              DishRepository dishRepository,
                              UserRepository userRepository) {
        this.productRepository = productRepository;
        this.validator = validator;
        this.pageValidator = pageValidator;
        this.dishRepository = dishRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Product getProductById(int id) {
        return productRepository.getProductById(id);
    }

    @Override
    public Product addProduct(Product product) {
        try {
            validator.validate(product);
            int id = productRepository.addProduct(product);
            if (id == -1) {
                throw new IllegalArgumentException("Unable to retrieve the generated key");
            }
            product = getProductById(id);
            Logger.info("Product was added:\n" + product);
            return product;
        } catch (IllegalArgumentException | DataAccessException exception) {
            Logger.warn("Product wasn't added: " + product + "\n" + exception.getMessage());
            return null;
        }
    }

    @Override
    public boolean deleteProductById(int id) {
        Product productToDel = getProductById(id);
        try {
            if (productToDel == null) {
                throw new IllegalArgumentException("Product with id '" + id + "' not found");
            }
            productRepository.deleteProductById(id);

            Logger.info("Product was deleted:\n" + productToDel);
            return true;
        } catch (IllegalArgumentException | DataAccessException exception) {
            Logger.warn("Product wasn't deleted: " + productToDel + "\n" + exception.getMessage());
            return false;
        }
    }

    @Override
    public Product updateProductById(Product product, int id) {
        try {
            if (getProductById(id) == null) {
                throw new IllegalArgumentException("Product with id '" + id + "' not found");
            }
            validator.validate(product);
            productRepository.updateProductById(product, id);

            Logger.info("Product was updated:\n" + product);
            return getProductById(id);
        } catch (IllegalArgumentException | DataAccessException exception) {
            Logger.warn("Product wasn't updated to: " + product + "\n" + exception.getMessage());
            return null;
        }
    }

    @Override
    public List<Product> getProducts() {
        return productRepository.getProducts();
    }

    @Override
    public int getCountOfPages(int limit, String input) {
        try {
            pageValidator.validatePageLimit(limit);
        } catch (IllegalArgumentException exception) {
            Logger.warn(exception.getMessage());
            return 1;
        }
        double count = getProductsNameContainsInput(input).size() / (double) limit;

        int moreCount = (int) Math.ceil(count);
        return moreCount != 0 ? moreCount : 1;
    }

    @Override
    public List<Product> getProductsNameSortedByPageAndInput(
        int limit,
        int pageNumber,
        String input
    ) {
        try {
            pageValidator.validatePageLimit(limit);
            pageValidator.validatePageNumber(pageNumber, getCountOfPages(limit, input));

            return productRepository.getProductsNameSortedByPageAndInput(limit, pageNumber, input);
        } catch (IllegalArgumentException exception) {
            Logger.warn(exception.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public List<Product> getProductsOfDish(int dishId) {
        if (dishRepository.getDishById(dishId) == null) {
            Logger.warn("Dish with id '" + dishId + "' not found");
            return new ArrayList<>();
        }
        return productRepository.getProductsByDishId(dishId);
    }

    @Override
    public List<Product> getUserAllergicProducts(int userId) {
        if (userRepository.getUserById(userId) == null) {
            Logger.warn("User with id '" + userId + "' not found");
            return new ArrayList<>();
        }
        return productRepository.getProductsByUserId(userId);
    }

    private List<Product> getProductsNameContainsInput(String input) {
        return getProducts().stream()
            .filter(product -> product.getName().toLowerCase().contains(input.toLowerCase()))
            .toList();
    }
}
