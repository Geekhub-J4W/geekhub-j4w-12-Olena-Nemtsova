package edu.geekhub.coursework.allergics.interfaces;

import edu.geekhub.coursework.allergics.UserAllergicProduct;

public interface UserAllergicProductRepository {

    void addRelation(UserAllergicProduct relation);

    UserAllergicProduct getRelationByUserAndProductId(int userId, int productId);

    void deleteRelationByUserAndProductId(int userId, int productId);
}
