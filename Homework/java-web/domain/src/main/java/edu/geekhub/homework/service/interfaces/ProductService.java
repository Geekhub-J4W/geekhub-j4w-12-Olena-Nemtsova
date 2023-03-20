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

    List<Product> getSortedProductsByCategoryWithPagination(ProductsSortType sortType,
                                                            int categoryId,
                                                            int limit,
                                                            int pageNumber);

    int getCountOfPages(int categoryId, int limit);

    List<Product> getProductsNameContainsInput(String input);
}
