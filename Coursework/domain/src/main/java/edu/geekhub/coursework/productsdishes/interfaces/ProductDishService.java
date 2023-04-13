package edu.geekhub.coursework.productsdishes.interfaces;

import edu.geekhub.coursework.productsdishes.ProductDish;

public interface ProductDishService {

    ProductDish getRelationByProductAndDishId(int productId, int dishId);

    ProductDish addRelation(ProductDish productDish);

    boolean deleteRelationByProductAndDishId(int productId, int dishId);

    ProductDish updateRelation(ProductDish productDish);
}
