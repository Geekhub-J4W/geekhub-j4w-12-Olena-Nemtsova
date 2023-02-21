package edu.geekhub.homework.domain;

import java.util.List;

public interface ProductService {
    boolean containsProduct(Product product);

    boolean addProduct(Product product);

    boolean deleteProductById(int id);

    boolean updateProductById(Product product, int id);

    List<Product> getProducts();

    List<Product> getSortedByNameProducts();

    List<Product> getSortedByPriceProducts();
}
