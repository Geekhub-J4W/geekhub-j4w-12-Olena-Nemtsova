package edu.geekhub.homework;

import edu.geekhub.homework.domain.Product;
import edu.geekhub.homework.domain.ProductService;
import java.util.List;

public class ApplicationStarter {

    public static void main(String[] args) {
        ProductConsoleParser productConsoleParser = new ProductConsoleParser();
        ProductService productService = new ProductService();

        List<String> inputs = List.of("Milk, 45.90", "Eggs, 58.70", "Bread, 15.40", "Bananas");
        for (String input : inputs) {
            Product product = productConsoleParser.parse(input);
            productService.addProduct(product);
        }

        Product product = new Product("MilkyWay", 15.80);
        productService.updateProductById(product, 2);

        productService.deleteProductById(2);
    }
}
