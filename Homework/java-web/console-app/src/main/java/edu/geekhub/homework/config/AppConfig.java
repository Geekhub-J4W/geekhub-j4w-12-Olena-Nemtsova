package edu.geekhub.homework.config;

import config.DomainConfig;
import edu.geekhub.homework.ProductConsoleParser;
import edu.geekhub.homework.controllers.BucketController;
import edu.geekhub.homework.controllers.ProductsController;
import edu.geekhub.homework.domain.BucketService;
import edu.geekhub.homework.domain.ProductService;
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
    public ProductsController productsController(ProductService productService,
                                                 ProductConsoleParser productConsoleParser) {
        return new ProductsController(productService, productConsoleParser);
    }

    @Lazy
    @Bean
    public BucketController bucketController(BucketService bucketService) {
        return new BucketController(bucketService);
    }
}
