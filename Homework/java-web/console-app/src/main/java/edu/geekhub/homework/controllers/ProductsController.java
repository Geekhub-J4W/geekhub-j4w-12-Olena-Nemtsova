package edu.geekhub.homework.controllers;

import edu.geekhub.homework.ProductConsoleParser;
import edu.geekhub.homework.products.Product;
import edu.geekhub.homework.products.interfaces.ProductService;
import java.util.List;

public class ProductsController {
    private final ProductService productService;
    private final ProductConsoleParser productConsoleParser;

    public ProductsController(ProductService productService,
                              ProductConsoleParser productConsoleParser) {
        this.productService = productService;
        this.productConsoleParser = productConsoleParser;
    }

    public Product addProduct(String line) {
        Product product = productConsoleParser.parse(line);
        return productService.addProduct(product);
    }

    public Product getProductById(int id) {
        return productService.getProductById(id);
    }

    public boolean deleteProduct(int id) {
        return productService.deleteProductById(id);
    }

    public Product updateProductById(Product product, int id) {
        return productService.updateProductById(product, id);
    }

    public List<Product> getProducts() {
        return productService.getProducts();
    }

}
