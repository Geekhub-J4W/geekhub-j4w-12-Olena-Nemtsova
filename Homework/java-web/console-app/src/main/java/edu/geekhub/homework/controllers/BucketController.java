package edu.geekhub.homework.controllers;

import edu.geekhub.homework.domain.Product;
import edu.geekhub.homework.service.BucketService;
import java.util.List;

public class BucketController {
    private final BucketService bucketService;

    public BucketController(BucketService bucketService) {
        this.bucketService = bucketService;
    }

    public boolean addProduct(Product product) {
        return bucketService.addProduct(product);
    }

    public boolean deleteProduct(int id) {
        return bucketService.deleteProduct(id);
    }

    public List<Product> getBucketProducts() {
        return bucketService.getBucketProducts();
    }

    public boolean checkout() {
        return bucketService.checkout();
    }
}
