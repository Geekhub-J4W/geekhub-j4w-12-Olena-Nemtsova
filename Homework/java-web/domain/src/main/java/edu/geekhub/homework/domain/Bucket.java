package edu.geekhub.homework.domain;

import edu.geekhub.homework.service.interfaces.ProductService;
import java.util.ArrayList;
import java.util.List;

public class Bucket {
    private final ProductService productService;
    private final List<Product> products;

    public Bucket(ProductService productService) {
        products = new ArrayList<>();
        this.productService = productService;
    }

    public List<Product> getBucketProducts() {
        return products;
    }

    public boolean addProduct(Product product) {
        if (productService.getProductById(product.id()) != null) {
            products.add(product);
            return true;
        }
        return false;
    }

    public boolean deleteProduct(int id) {
        return products.removeIf(product -> product.id() == id);
    }

    public void clearBucket() {
        products.clear();
    }
}
