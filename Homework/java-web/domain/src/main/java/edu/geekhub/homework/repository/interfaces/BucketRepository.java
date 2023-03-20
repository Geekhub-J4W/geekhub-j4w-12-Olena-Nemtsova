package edu.geekhub.homework.repository.interfaces;

import edu.geekhub.homework.domain.Product;
import java.util.List;

public interface BucketRepository {
    int addBucketProduct(int productId, String userId);

    void deleteUserBucketProductsById(String id);

    int deleteUserBucketProductById(int productId, String userId);

    int deleteUserBucketOneProductById(int productId, String userId);

    List<Product> getBucketProductsByUserId(String id);
}
