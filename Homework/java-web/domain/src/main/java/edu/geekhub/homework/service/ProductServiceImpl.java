package edu.geekhub.homework.service;

import edu.geekhub.homework.domain.Category;
import edu.geekhub.homework.domain.Product;
import edu.geekhub.homework.domain.ProductValidator;
import edu.geekhub.homework.repository.interfaces.ProductRepository;
import edu.geekhub.homework.service.interfaces.CategoryService;
import edu.geekhub.homework.service.interfaces.ProductService;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import org.springframework.dao.DataAccessException;
import org.tinylog.Logger;

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
        return getProducts().stream()
            .filter(product -> product.id() == id)
            .findFirst()
            .orElse(null);
    }

    @Override
    public boolean addProduct(Product product) {
        boolean successAdd = false;
        try {
            productValidator.validate(product);
            int id = productRepository.addProduct(product);
            if (id == -1) {
                throw new IllegalArgumentException("Unable to retrieve the generated key");
            }
            product = product.changeId(id);
            Logger.info("Product was added:\n" + product);
            successAdd = true;
        } catch (IllegalArgumentException | DataAccessException exception) {
            Logger.warn("Product wasn't added: " + product + "\n" + exception.getMessage());
        }
        return successAdd;
    }

    @Override
    public boolean deleteProductById(int id) {
        Product productToDel = getProductById(id);
        try {
            if (productToDel == null) {
                throw new IllegalArgumentException("Product with id" + id + "not found");
            }
            productRepository.deleteProductById(id);
            return true;
        } catch (IllegalArgumentException | DataAccessException exception) {
            Logger.warn("Product wasn't deleted: " + productToDel + "\n" + exception.getMessage());
            return false;
        }
    }

    @Override
    public boolean updateProductById(Product product, int id) {
        try {
            productValidator.validate(product);
            if (getProductById(id) == null) {
                throw new IllegalArgumentException("Product with id" + id + "not found");
            }
            productRepository.updateProductById(product, id);
            return true;
        } catch (IllegalArgumentException | DataAccessException exception) {
            Logger.warn("Product wasn't edited: " + product + "\n" + exception.getMessage());
            return false;
        }
    }

    @Override
    public List<Product> getProducts() {
        return productRepository.getProducts();
    }

    @Override
    public List<Product> getSortedByNameProducts() {
        List<Product> sortedProducts = new ArrayList<>(getProducts());
        sortedProducts.sort((Comparator.comparing(Product::name)));
        return sortedProducts;
    }

    @Override
    public List<Product> getSortedByPriceProducts() {
        List<Product> sortedProducts = new ArrayList<>(getProducts());
        sortedProducts.sort((Comparator.comparingDouble(Product::price)));
        return sortedProducts;
    }

    @Override
    public List<Product> getProductsRatingSorted() {
        return productRepository.getProductsRatingSorted();
    }

    @Override
    public List<Product> getProductsRatingSortedByCategory(int categoryId) {
        Category category = categoryService.getCategoryById(categoryId);
        if (category == null) {
            Logger.warn("Can't sort by not exists category with id " + categoryId);
            return new ArrayList<>();
        }

        List<Product> products = getProductsRatingSorted();
        return products.stream()
            .filter(product -> product.categoryId() == categoryId)
            .toList();
    }
}
