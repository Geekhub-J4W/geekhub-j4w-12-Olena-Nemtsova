package edu.geekhub.homework.repository.interfaces;

import edu.geekhub.homework.domain.Product;
import java.util.List;

public interface ProductRepository {
    List<Product> getProductsRatingSorted();

    int addProduct(Product product);

    List<Product> getProducts();

    Product getProductById(int id);

    void deleteProductById(int id);

    void updateProductById(Product product, int id);
}
