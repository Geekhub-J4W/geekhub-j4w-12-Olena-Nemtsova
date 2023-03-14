package edu.geekhub.homework.service.interfaces;

import edu.geekhub.homework.domain.Product;
import edu.geekhub.homework.domain.ProductsSortType;
import java.util.List;

public interface ProductService {

    Product getProductById(int id);

    Product addProduct(Product product);

    boolean deleteProductById(int id);

    Product updateProductById(Product product, int id);

    List<Product> getProducts();

    List<Product> getSortedByNameProducts();

    List<Product> getSortedByPriceProducts();

    List<Product> getProductsRatingSorted();

    List<Product> getSortedProducts(ProductsSortType sortType, int categoryId);
}
