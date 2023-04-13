package edu.geekhub.coursework.productsdishes.interfaces;

import edu.geekhub.coursework.productsdishes.ProductDish;

public interface ProductDishRepository {

    void addRelation(ProductDish productDish);

    ProductDish getRelationByProductAndDishId(int productId, int dishId);

    void deleteRelationByProductAndDishId(int productId, int dishId);

    void updateRelationByProductAndDishId(ProductDish productDish);
}
