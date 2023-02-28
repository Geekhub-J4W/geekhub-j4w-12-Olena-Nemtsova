package edu.geekhub.homework.config;

import edu.geekhub.homework.domain.Bucket;
import edu.geekhub.homework.domain.CategoryValidator;
import edu.geekhub.homework.domain.ProductOrderValidator;
import edu.geekhub.homework.domain.ProductValidator;
import edu.geekhub.homework.repository.CategoryRepositoryImpl;
import edu.geekhub.homework.repository.OrderRepositoryImpl;
import edu.geekhub.homework.repository.ProductOrderRepositoryImpl;
import edu.geekhub.homework.repository.ProductRepositoryImpl;
import edu.geekhub.homework.repository.interfaces.CategoryRepository;
import edu.geekhub.homework.repository.interfaces.OrderRepository;
import edu.geekhub.homework.repository.interfaces.ProductOrderRepository;
import edu.geekhub.homework.repository.interfaces.ProductRepository;
import edu.geekhub.homework.service.BucketService;
import edu.geekhub.homework.service.CategoryServiceImpl;
import edu.geekhub.homework.service.OrderServiceImpl;
import edu.geekhub.homework.service.ProductOrderServiceImpl;
import edu.geekhub.homework.service.ProductServiceImpl;
import edu.geekhub.homework.service.interfaces.CategoryService;
import edu.geekhub.homework.service.interfaces.OrderService;
import edu.geekhub.homework.service.interfaces.ProductOrderService;
import edu.geekhub.homework.service.interfaces.ProductService;
import java.io.File;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Lazy;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

@Configuration
@Import(DatabaseConfig.class)
public class DomainConfig {
    @Bean
    public CategoryValidator categoryValidator() {
        return new CategoryValidator();
    }

    @Bean
    public CategoryRepository categoryRepository(NamedParameterJdbcTemplate jdbcTemplate,
                                                 CategoryValidator categoryValidator) {
        return new CategoryRepositoryImpl(jdbcTemplate, categoryValidator);
    }

    @Bean
    public ProductValidator productValidator(CategoryRepository categoryRepository) {
        return new ProductValidator(categoryRepository);
    }

    @Bean
    public ProductRepository productRepository(NamedParameterJdbcTemplate jdbcTemplate,
                                               ProductValidator productValidator) {
        return new ProductRepositoryImpl(jdbcTemplate, productValidator);
    }

    @Bean
    public ProductService productService(ProductRepository productRepository,
                                         ProductValidator productValidator,
                                         CategoryService categoryService) {
        return new ProductServiceImpl(productRepository, productValidator, categoryService);
    }

    @Bean
    public CategoryService categoryService(CategoryRepository categoryRepository,
                                           CategoryValidator categoryValidator) {
        return new CategoryServiceImpl(categoryRepository, categoryValidator);
    }

    @Lazy
    @Bean
    public ProductOrderValidator productOrderValidator(ProductRepository productRepository,
                                                       OrderRepository orderRepository) {
        return new ProductOrderValidator(productRepository, orderRepository);
    }

    @Lazy
    @Bean
    public OrderRepository orderRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        return new OrderRepositoryImpl(jdbcTemplate);
    }

    @Lazy
    @Bean
    public OrderService orderService(OrderRepository orderRepository) {
        return new OrderServiceImpl(orderRepository,
            new File("Homework/java-web/console-app/src/main/resources/checks.txt"));
    }

    @Lazy
    @Bean
    public ProductOrderRepository productOrderRepository(NamedParameterJdbcTemplate jdbcTemplate,
                                                         ProductOrderValidator validator) {
        return new ProductOrderRepositoryImpl(jdbcTemplate, validator);
    }

    @Lazy
    @Bean
    public ProductOrderService productOrderService(ProductOrderRepository productOrderRepository,
                                                   ProductOrderValidator validator) {
        return new ProductOrderServiceImpl(productOrderRepository, validator);
    }

    @Lazy
    @Bean
    public Bucket bucket(ProductService productService) {
        return new Bucket(productService);
    }

    @Lazy
    @Bean
    public BucketService bucketService(ProductService productService,
                                       OrderService orderService,
                                       ProductOrderService productOrderService,
                                       Bucket bucket) {
        return new BucketService(productService, orderService, productOrderService, bucket);
    }
}
