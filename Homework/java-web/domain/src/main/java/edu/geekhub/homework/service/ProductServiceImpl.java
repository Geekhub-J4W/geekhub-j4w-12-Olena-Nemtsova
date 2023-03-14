package edu.geekhub.homework.service;

import edu.geekhub.homework.domain.Category;
import edu.geekhub.homework.domain.Product;
import edu.geekhub.homework.domain.ProductValidator;
import edu.geekhub.homework.domain.ProductsSortType;
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
            .filter(product -> product.getId() == id)
            .findFirst()
            .orElse(null);
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
                throw new IllegalArgumentException("Product with id" + id + "not found");
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
                throw new IllegalArgumentException("Product with id" + id + "not found");
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

    @Override
    public List<Product> getSortedByNameProducts() {
        List<Product> sortedProducts = new ArrayList<>(getProducts());
        sortedProducts.sort((Comparator.comparing(Product::getName)));
        return sortedProducts;
    }

    @Override
    public List<Product> getSortedByPriceProducts() {
        List<Product> sortedProducts = new ArrayList<>(getProducts());
        sortedProducts.sort((Comparator.comparingDouble(Product::getPrice)));
        return sortedProducts;
    }

    @Override
    public List<Product> getProductsRatingSorted() {
        return productRepository.getProductsRatingSorted();
    }

    @Override
    public List<Product> getSortedProducts(ProductsSortType sortType, int categoryId) {
        List<Product> products;
        switch (sortType) {
            case NAME -> products = getSortedByNameProducts();
            case PRICE -> products = getSortedByPriceProducts();
            case RATING -> products = getProductsRatingSorted();
            default -> products = getProducts();
        }
        Category category = categoryService.getCategoryById(categoryId);
        if (category != null) {
            products = products.stream()
                .filter(product -> product.getCategoryId() == categoryId)
                .toList();
        }
        return products;
    }
}
