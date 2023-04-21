package edu.geekhub.coursework.allergics;

import edu.geekhub.coursework.allergics.interfaces.UserAllergicProductRepository;
import edu.geekhub.coursework.allergics.interfaces.UserAllergicProductService;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.tinylog.Logger;

@Service
public class UserAllergicProductServiceImpl implements UserAllergicProductService {
    private final UserAllergicProductValidator validator;
    private final UserAllergicProductRepository userAllergicProductRepository;

    public UserAllergicProductServiceImpl(
        UserAllergicProductValidator validator,
        UserAllergicProductRepository userAllergicProductRepository
    ) {
        this.validator = validator;
        this.userAllergicProductRepository = userAllergicProductRepository;
    }

    @Override
    public UserAllergicProduct getRelationByUserAndProductId(int userId, int productId) {
        return userAllergicProductRepository.getRelationByUserAndProductId(userId, productId);
    }

    @Override
    public UserAllergicProduct addRelation(UserAllergicProduct relation) {
        try {
            validator.validate(relation);
            int userId = relation.getUserId();
            int productId = relation.getProductId();

            if (getRelationByUserAndProductId(userId, productId) != null) {
                throw new IllegalArgumentException("Relation already exists");
            }
            userAllergicProductRepository.addRelation(relation);

            relation = getRelationByUserAndProductId(userId, productId);
            Logger.info("UserAllergicProduct relation was added:\n" + relation);
            return relation;
        } catch (IllegalArgumentException | DataAccessException exception) {
            Logger.warn("UserAllergicProduct relation wasn't added: "
                        + relation + "\n" + exception.getMessage());
            return null;
        }
    }

    @Override
    public boolean deleteRelationByUserAndProductId(int userId, int productId) {
        UserAllergicProduct relationToDel = getRelationByUserAndProductId(userId, productId);
        try {
            if (relationToDel == null) {
                throw new IllegalArgumentException("UserAllergicProduct relation with userId '"
                                                   + userId + "' and productId '"
                                                   + productId + "' not found");
            }
            userAllergicProductRepository.deleteRelationByUserAndProductId(userId, productId);

            Logger.info("UserAllergicProduct relation was deleted:\n" + relationToDel);
            return true;
        } catch (IllegalArgumentException | DataAccessException exception) {
            Logger.warn("UserAllergicProduct relation wasn't deleted: " + relationToDel
                        + "\n" + exception.getMessage());
            return false;
        }
    }
}
