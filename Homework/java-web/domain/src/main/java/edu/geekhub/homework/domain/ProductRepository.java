package edu.geekhub.homework.domain;

import java.util.List;

public interface ProductRepository {
    void addProduct(Product product);

    List<Product> getProducts();

    Product getProductById(int id);

    boolean deleteProductById(int id);

    boolean updateProductById(Product product, int id);
}
