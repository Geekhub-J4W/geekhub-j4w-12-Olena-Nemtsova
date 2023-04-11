package edu.geekhub.homework.products.interfaces;

import edu.geekhub.homework.products.Product;
import java.util.List;

public interface ProductRepository {

    int addProduct(Product product);

    List<Product> getProducts();

    Product getProductById(int id);

    void deleteProductById(int id);

    void updateProductById(Product product, int id);

    List<Product> getProductsOrdersSortedWithPagination(int limit,
                                                        int pageNumber,
                                                        List<Integer> categoriesId);

    List<Product> getProductsRatingSortedWithPagination(int limit,
                                                        int pageNumber,
                                                        List<Integer> categoriesId);

    List<Product> getProductsNameSortedWithPagination(int limit,
                                                      int pageNumber,
                                                      List<Integer> categoriesId);

    List<Product> getProductsPriceSortedWithPagination(int limit,
                                                       int pageNumber,
                                                       List<Integer> categoriesId);
}
