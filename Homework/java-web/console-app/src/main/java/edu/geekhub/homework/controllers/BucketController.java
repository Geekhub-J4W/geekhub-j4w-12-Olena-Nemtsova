package edu.geekhub.homework.controllers;

import edu.geekhub.homework.buckets.BucketService;
import edu.geekhub.homework.products.Product;
import java.util.List;

public class BucketController {
    private final BucketService bucketService;

    public BucketController(BucketService bucketService) {
        this.bucketService = bucketService;
    }

    public boolean addProduct(Product product, int userId) {
        return bucketService.addProduct(product.getId(), userId);
    }

    public boolean deleteProduct(int productId, int userId) {
        return bucketService.deleteAllConcreteProducts(productId, userId);
    }

    public List<Product> getBucketProducts(int userId) {
        return bucketService.getBucketProducts(userId);
    }

}
