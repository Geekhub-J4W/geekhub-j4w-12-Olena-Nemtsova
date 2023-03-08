package edu.geekhub.homework;

import edu.geekhub.homework.config.AppConfig;
import edu.geekhub.homework.config.DatabaseConfig;
import edu.geekhub.homework.config.DomainConfig;
import edu.geekhub.homework.controllers.CategoriesController;
import edu.geekhub.homework.controllers.ProductsController;
import java.util.List;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

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
    }
}
