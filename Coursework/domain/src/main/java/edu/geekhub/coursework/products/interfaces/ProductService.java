package edu.geekhub.coursework.products.interfaces;

import edu.geekhub.coursework.products.Product;
import java.util.List;

public interface ProductService {

    Product getProductById(int id);

    Product addProduct(Product product);

    boolean deleteProductById(int id);

    Product updateProductById(Product product, int id);

    List<Product> getProducts();

    int getCountOfPages(int limit, String input);

    List<Product> getProductsNameSortedByPageAndInput(int limit, int pageNumber, String input);

    List<Product> getProductsOfDish(int dishId);

    List<Product> getUserAllergicProducts(int userId);
}
