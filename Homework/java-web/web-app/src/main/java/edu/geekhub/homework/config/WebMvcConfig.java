package edu.geekhub.homework.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry
            .addResourceHandler("/**")
            .addResourceLocations("classpath:/templates/");
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/login");
        registry.addViewController("/mainUser");
        registry.addViewController("/main");
        registry.addViewController("/personal");
        registry.addViewController("/product");
        registry.addViewController("/checkout");
        registry.addViewController("/register");
        registry.addViewController("/newProduct");
        registry.addViewController("/newCategory");
        registry.addViewController("/newUser");
        registry.addViewController("/newOrder");
        registry.addViewController("/error");
    }
}
