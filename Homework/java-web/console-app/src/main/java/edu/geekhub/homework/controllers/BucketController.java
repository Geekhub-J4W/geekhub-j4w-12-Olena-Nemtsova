package edu.geekhub.homework.controllers;

import edu.geekhub.homework.domain.Product;
import edu.geekhub.homework.service.BucketService;
import java.util.List;

public class BucketController {
    private final BucketService bucketService;

    public BucketController(BucketService bucketService) {
        this.bucketService = bucketService;
    }

    public boolean addProduct(Product product, String userId) {
        return bucketService.addProduct(product, userId);
    }

    public boolean deleteProduct(int productId, String userId) {
        return bucketService.deleteProduct(productId, userId);
    }

    public List<Product> getBucketProducts(String userId) {
        return bucketService.getBucketProducts(userId);
    }

    public String checkout(String userId) {
        return bucketService.checkout(userId);
    }
}
