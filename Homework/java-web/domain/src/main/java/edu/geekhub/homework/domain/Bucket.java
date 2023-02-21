package edu.geekhub.homework.domain;

import java.util.List;

public class Bucket {
    private final ProductService productService;
    private final ProductRepository productRepository;

    public Bucket(ProductService productService, ProductRepository productRepository) {
        this.productRepository = productRepository;
        this.productService = productService;
    }

    public List<Product> getBucketProducts() {
        return productRepository.getProducts();
    }

    public boolean addProduct(Product product) {
        if (productService.containsProduct(product)) {
            productRepository.addProduct(product);
            return true;
        }
        return false;
    }

    public boolean deleteProduct(int id) {
        return productRepository.deleteProductById(id);
    }

    public void clearBucket() {
        productRepository.getProducts().clear();
    }
}
