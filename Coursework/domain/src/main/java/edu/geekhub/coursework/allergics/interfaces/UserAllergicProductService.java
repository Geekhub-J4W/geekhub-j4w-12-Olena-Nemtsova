package edu.geekhub.coursework.allergics.interfaces;

import edu.geekhub.coursework.allergics.UserAllergicProduct;

public interface UserAllergicProductService {

    UserAllergicProduct getRelationByUserAndProductId(int userId, int productId);

    UserAllergicProduct addRelation(UserAllergicProduct relation);

    boolean deleteRelationByUserAndProductId(int userId, int productId);
}
