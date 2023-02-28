package edu.geekhub.homework;

import edu.geekhub.homework.config.AppConfig;
import edu.geekhub.homework.config.DatabaseConfig;
import edu.geekhub.homework.config.DomainConfig;
import edu.geekhub.homework.controllers.BucketController;
import edu.geekhub.homework.controllers.CategoriesController;
import edu.geekhub.homework.controllers.ProductsController;
import edu.geekhub.homework.domain.Product;
import java.util.List;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.tinylog.Logger;

public class ApplicationStarter {

    public static void main(String[] args) {
        var applicationContext = new AnnotationConfigApplicationContext(
            DatabaseConfig.class,
            DomainConfig.class,
            AppConfig.class
        );

        var productController = applicationContext.getBean(ProductsController.class);
        var categoryController = applicationContext.getBean(CategoriesController.class);

        List<String> inputCategories = List.of("Dairy", "Bakery");
        for (String input : inputCategories) {
            categoryController.addCategory(input);
        }

        List<String> inputProducts = List.of("Milk, 45.90, 1", "Eggs, 58.70, 1", "Bread, 15.40, 2");
        for (String input : inputProducts) {
            productController.addProduct(input);
        }

        var bucketController = applicationContext.getBean(BucketController.class);
        bucketController.addProduct(productController.getProductById(1));
        bucketController.addProduct(productController.getProductById(3));
        bucketController.checkout();

        bucketController.addProduct(productController.getProductById(3));
        bucketController.checkout();

        List<Product> sortedByRatingProducts = productController.getRatingSortedProducts();
        for (Product product : sortedByRatingProducts) {
            Logger.info(product);
        }
    }
}
