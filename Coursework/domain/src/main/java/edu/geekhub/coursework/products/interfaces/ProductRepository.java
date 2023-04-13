package edu.geekhub.coursework.products.interfaces;

import edu.geekhub.coursework.products.Product;
import java.util.List;

public interface ProductRepository {

    List<Product> getProducts();

    int addProduct(Product product);

    Product getProductById(int id);

    void deleteProductById(int id);

    void updateProductById(Product product, int id);

    List<Product> getProductsByDishId(int dishId);

    List<Product> getProductsByUserId(int userId);

    List<Product> getProductsNameSortedByPageAndInput(int limit, int pageNumber, String input);
}
