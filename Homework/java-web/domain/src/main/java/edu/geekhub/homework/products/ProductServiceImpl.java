package edu.geekhub.homework.products;

import edu.geekhub.homework.categories.Category;
import edu.geekhub.homework.categories.interfaces.CategoryService;
import edu.geekhub.homework.products.interfaces.ProductRepository;
import edu.geekhub.homework.products.interfaces.ProductService;
import java.util.ArrayList;
import java.util.List;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.tinylog.Logger;

@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ProductValidator productValidator;
    private final CategoryService categoryService;

    public ProductServiceImpl(ProductRepository productRepository,
                              ProductValidator productValidator,
                              CategoryService categoryService) {
        this.productRepository = productRepository;
        this.productValidator = productValidator;
        this.categoryService = categoryService;
    }

    @Override
    public Product getProductById(int id) {
        return productRepository.getProductById(id);
    }

    @Override
    public Product addProduct(Product product) {
        try {
            productValidator.validate(product);
            int id = productRepository.addProduct(product);
            if (id == -1) {
                throw new IllegalArgumentException("Unable to retrieve the generated key");
            }
            product.setId(id);
            Logger.info("Product was added:\n" + product);
            return getProductById(id);
        } catch (IllegalArgumentException | DataAccessException exception) {
            Logger.warn("Product wasn't added: " + product + "\n" + exception.getMessage());
            return null;
        }
    }

    @Override
    public boolean deleteProductById(int id) {
        Product productToDel = getProductById(id);
        try {
            if (productToDel == null) {
                throw new IllegalArgumentException("Product with id '" + id + "' not found");
            }
            productRepository.deleteProductById(id);
            Logger.info("Product was deleted:\n" + productToDel);
            return true;
        } catch (IllegalArgumentException | DataAccessException exception) {
            Logger.warn("Product wasn't deleted: " + productToDel + "\n" + exception.getMessage());
            return false;
        }
    }

    @Override
    public Product updateProductById(Product product, int id) {
        try {
            productValidator.validate(product);
            if (getProductById(id) == null) {
                throw new IllegalArgumentException("Product with id '" + id + "' not found");
            }
            productRepository.updateProductById(product, id);
            Logger.info("Product was updated:\n" + product);
            return getProductById(id);
        } catch (IllegalArgumentException | DataAccessException exception) {
            Logger.warn("Product wasn't updated: " + product + "\n" + exception.getMessage());
            return null;
        }
    }

    @Override
    public List<Product> getProducts() {
        return productRepository.getProducts();
    }

    private List<Product> getSortedByNameProducts(int limit,
                                                  int pageNumber,
                                                  List<Integer> categoriesId) {

        return productRepository
            .getProductsNameSortedWithPagination(limit, pageNumber, categoriesId);
    }

    private List<Product> getSortedByPriceProducts(int limit,
                                                   int pageNumber,
                                                   List<Integer> categoriesId) {

        return productRepository
            .getProductsPriceSortedWithPagination(limit, pageNumber, categoriesId);
    }

    private List<Product> getProductsOrdersSorted(int limit,
                                                  int pageNumber,
                                                  List<Integer> categoriesId) {

        return productRepository
            .getProductsOrdersSortedWithPagination(limit, pageNumber, categoriesId);
    }

    private List<Product> getProductsRatingSorted(int limit,
                                                  int pageNumber,
                                                  List<Integer> categoriesId) {

        return productRepository
            .getProductsRatingSortedWithPagination(limit, pageNumber, categoriesId);
    }

    @Override
    public List<Product> getSortedProductsByCategoryWithPagination(ProductsSortType sortType,
                                                                   int categoryId,
                                                                   int limit,
                                                                   int pageNumber) {
        Category category = categoryService.getCategoryById(categoryId);
        List<Integer> categoriesId = new ArrayList<>();
        if (category != null) {
            categoriesId.add(categoryId);
        } else {
            categoriesId = categoryService.getCategories().stream().map(Category::getId).toList();
        }

        List<Product> products;
        limit = validateLimit(limit);
        sortType = sortType != null ? sortType : ProductsSortType.RATING;
        switch (sortType) {
            case NAME -> products = getSortedByNameProducts(limit, pageNumber, categoriesId);
            case PRICE -> products = getSortedByPriceProducts(limit, pageNumber, categoriesId);
            case ORDERS -> products = getProductsOrdersSorted(limit, pageNumber, categoriesId);
            default -> products = getProductsRatingSorted(limit, pageNumber, categoriesId);
        }
        return products;
    }

    @Override
    public int getCountOfPages(int categoryId, int limit) {
        double count;
        limit = validateLimit(limit);

        Category category = categoryService.getCategoryById(categoryId);
        if (category != null) {
            count = getProducts().stream()
                        .filter(product -> product.getCategoryId() == categoryId)
                        .toList().size() / (double) limit;
        } else {
            count = getProducts().size() / (double) limit;
        }

        return (int) Math.ceil(count);
    }

    private int validateLimit(int limit) {
        if (limit <= 0) {
            Logger.warn("Limit of products on a page can't be less than 1");
            return 1;
        }
        return limit;
    }

    @Override
    public List<Product> getProductsNameContainsInput(String input) {
        return getProducts().stream()
            .filter(product -> product.getName().toLowerCase().contains(input.toLowerCase()))
            .toList();
    }
}
