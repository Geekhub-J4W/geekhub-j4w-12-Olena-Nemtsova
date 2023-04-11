package edu.geekhub.homework.buckets;

import edu.geekhub.homework.products.Product;
import java.util.List;

public interface BucketRepository {
    int addBucketProduct(int productId, int userId);

    void deleteUserBucketProductsById(int id);

    int deleteUserBucketProductById(int productId, int userId);

    int deleteUserBucketOneProductById(int productId, int userId);

    List<Product> getBucketProductsByUserId(int id);
}
