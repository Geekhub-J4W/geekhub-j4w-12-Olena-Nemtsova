package edu.geekhub.homework.products.interfaces;

import edu.geekhub.homework.products.Product;
import edu.geekhub.homework.products.ProductsSortType;
import java.util.List;

public interface ProductService {

    Product getProductById(int id);

    Product addProduct(Product product);

    boolean deleteProductById(int id);

    Product updateProductById(Product product, int id);

    List<Product> getProducts();

    List<Product> getSortedProductsByCategoryWithPagination(ProductsSortType sortType,
                                                            int categoryId,
                                                            int limit,
                                                            int pageNumber);

    int getCountOfPages(int categoryId, int limit);

    List<Product> getProductsNameContainsInput(String input);
}
