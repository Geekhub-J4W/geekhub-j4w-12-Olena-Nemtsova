package edu.geekhub.coursework.productsdishes;

import edu.geekhub.coursework.productsdishes.interfaces.ProductDishRepository;
import edu.geekhub.coursework.productsdishes.interfaces.ProductDishService;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.tinylog.Logger;

@Service
public class ProductDishServiceImpl implements ProductDishService {
    private final ProductDishValidator validator;
    private final ProductDishRepository productDishRepository;

    public ProductDishServiceImpl(ProductDishValidator validator,
                                  ProductDishRepository productDishRepository) {
        this.validator = validator;
        this.productDishRepository = productDishRepository;
    }

    @Override
    public ProductDish getRelationByProductAndDishId(int productId, int dishId) {
        return productDishRepository.getRelationByProductAndDishId(productId, dishId);
    }

    @Override
    public ProductDish addRelation(ProductDish productDish) {
        try {
            validator.validate(productDish);
            productDishRepository.addRelation(productDish);

            productDish = getRelationByProductAndDishId(
                productDish.getProductId(),
                productDish.getDishId()
            );

            Logger.info("ProductDish relation was added:\n" + productDish);
            return productDish;
        } catch (IllegalArgumentException | DataAccessException exception) {
            Logger.warn("ProductDish relation wasn't added: "
                        + productDish + "\n" + exception.getMessage());
            return null;
        }
    }

    @Override
    public boolean deleteRelationByProductAndDishId(int productId, int dishId) {
        ProductDish productDishToDel = getRelationByProductAndDishId(productId, dishId);
        try {
            if (productDishToDel == null) {
                throw new IllegalArgumentException(
                    "ProductDish relation with productId '" + productId
                    + "' and dishId '" + dishId
                    + "' not found"
                );
            }
            productDishRepository.deleteRelationByProductAndDishId(productId, dishId);

            Logger.info("ProductDish relation was deleted:\n" + productDishToDel);
            return true;
        } catch (IllegalArgumentException | DataAccessException exception) {
            Logger.warn("ProductDish relation wasn't deleted: "
                        + productDishToDel + "\n" + exception.getMessage());
            return false;
        }
    }

    @Override
    public ProductDish updateRelation(ProductDish productDish) {
        try {
            int productId = productDish.getProductId();
            int dishId = productDish.getDishId();
            if (getRelationByProductAndDishId(productId, dishId) == null) {
                throw new IllegalArgumentException(
                    "ProductDish with productId '" + productDish.getProductId()
                    + "' and dishId '" + productDish.getDishId()
                    + "' not found"
                );
            }
            validator.validate(productDish);
            productDishRepository.updateRelationByProductAndDishId(productDish);

            Logger.info("ProductDish relation was updated:\n" + productDish);
            return getRelationByProductAndDishId(productId, dishId);
        } catch (IllegalArgumentException | DataAccessException exception) {
            Logger.warn("ProductDish wasn't updated to: " + productDish
                        + "\n" + exception.getMessage());
            return null;
        }
    }
}
