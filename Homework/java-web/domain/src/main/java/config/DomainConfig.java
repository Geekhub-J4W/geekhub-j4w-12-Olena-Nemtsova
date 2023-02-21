package config;

import edu.geekhub.homework.domain.Bucket;
import edu.geekhub.homework.domain.BucketService;
import edu.geekhub.homework.domain.Order;
import edu.geekhub.homework.domain.OrderRepository;
import edu.geekhub.homework.domain.Product;
import edu.geekhub.homework.domain.ProductRepository;
import edu.geekhub.homework.domain.ProductRepositoryImpl;
import edu.geekhub.homework.domain.ProductService;
import edu.geekhub.homework.domain.ProductServiceImpl;
import edu.geekhub.homework.domain.ProductValidator;
import java.io.File;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;

@Configuration
public class DomainConfig {
    @Bean
    public ProductValidator productValidator() {
        return new ProductValidator();
    }

    @Bean
    @Scope("prototype")
    public ProductRepository productRepository(List<Product> products,
                                               ProductValidator productValidator) {
        return new ProductRepositoryImpl(products, productValidator);
    }

    @Bean
    public ProductService productService(ProductRepository productRepository,
                                         ProductValidator productValidator) {
        return new ProductServiceImpl(productRepository, productValidator);
    }

    @Lazy
    @Bean
    public OrderRepository orderRepository(List<Order> orders) {
        return new OrderRepository(orders,
            new File("Homework/java-web/console-app/src/main/resources/checks.txt"));
    }

    @Lazy
    @Bean
    public Bucket bucket(ProductService productService, ProductRepository productRepository) {
        return new Bucket(productService, productRepository);
    }

    @Lazy
    @Bean
    public BucketService bucketService(ProductService productService,
                                       OrderRepository orderRepository,
                                       Bucket bucket) {
        return new BucketService(productService, orderRepository, bucket);
    }
}
