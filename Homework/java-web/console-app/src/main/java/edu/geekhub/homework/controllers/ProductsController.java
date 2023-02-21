package edu.geekhub.homework.controllers;

import edu.geekhub.homework.ProductConsoleParser;
import edu.geekhub.homework.domain.Product;
import edu.geekhub.homework.domain.ProductService;
import java.util.List;

public class ProductsController {
    private final ProductService productService;
    private final ProductConsoleParser productConsoleParser;

    public ProductsController(ProductService productService,
                              ProductConsoleParser productConsoleParser) {
        this.productService = productService;
        this.productConsoleParser = productConsoleParser;
    }

    public boolean addProduct(String line) {
        Product product = productConsoleParser.parse(line);
        return productService.addProduct(product);
    }

    public boolean deleteProduct(int id) {
        return productService.deleteProductById(id);
    }

    public boolean updateProductById(Product product, int id) {
        return productService.updateProductById(product, id);
    }

    public List<Product> getProducts() {
        return productService.getProducts();
    }

    public List<Product> getSortedByNameProducts() {
        return productService.getSortedByNameProducts();
    }

    public List<Product> getSortedByPriceProducts() {
        return productService.getSortedByPriceProducts();
    }
}
