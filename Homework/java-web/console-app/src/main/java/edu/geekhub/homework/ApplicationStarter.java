package edu.geekhub.homework;

import config.DomainConfig;
import edu.geekhub.homework.config.AppConfig;
import edu.geekhub.homework.controllers.ProductsController;
import edu.geekhub.homework.domain.BucketService;
import java.util.List;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class ApplicationStarter {

    public static void main(String[] args) {
        var applicationContext = new AnnotationConfigApplicationContext(
            DomainConfig.class,
            AppConfig.class
        );

        var productController = applicationContext.getBean(ProductsController.class);

        List<String> inputs = List.of("Milk, 45.90", "Eggs, 58.70", "Bread, 15.40", "Bananas");
        for (String input : inputs) {
            productController.addProduct(input);
        }

        var bucketService = applicationContext.getBean(BucketService.class);
        bucketService.addProduct(productController.getProducts().get(0));
        bucketService.addProduct(productController.getProducts().get(2));
        bucketService.checkout();
    }
}
