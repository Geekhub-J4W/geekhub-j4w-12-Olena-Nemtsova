package edu.geekhub.homework.config;

import edu.geekhub.homework.CategoryConsoleParser;
import edu.geekhub.homework.ProductConsoleParser;
import edu.geekhub.homework.controllers.BucketController;
import edu.geekhub.homework.controllers.CategoriesController;
import edu.geekhub.homework.controllers.ProductsController;
import edu.geekhub.homework.service.BucketService;
import edu.geekhub.homework.service.interfaces.CategoryService;
import edu.geekhub.homework.service.interfaces.ProductService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Lazy;

@Configuration
@Import(DomainConfig.class)
public class AppConfig {

    @Bean
    public ProductConsoleParser productConsoleParser() {
        return new ProductConsoleParser();
    }

    @Bean
    public CategoryConsoleParser categoryConsoleParser() {
        return new CategoryConsoleParser();
    }

    @Bean
    public ProductsController productsController(ProductService productService,
                                                 ProductConsoleParser productConsoleParser) {
        return new ProductsController(productService, productConsoleParser);
    }

    @Lazy
    @Bean
    public BucketController bucketController(BucketService bucketService) {
        return new BucketController(bucketService);
    }

    @Bean
    public CategoriesController categoriesController(CategoryService categoryService,
                                                   CategoryConsoleParser consoleParser) {
        return new CategoriesController(categoryService, consoleParser);
    }
}
