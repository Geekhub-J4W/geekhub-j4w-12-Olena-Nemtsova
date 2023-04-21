package edu.geekhub.coursework.config;

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
        registry.addViewController("/main/products");
        registry.addViewController("/main/dishes");
        registry.addViewController("/main/dish");
        registry.addViewController("/main/allergic");
        registry.addViewController("/main/parameters");
        registry.addViewController("/main/ownDiet");
        registry.addViewController("/login");
        registry.addViewController("/register");
        registry.addViewController("/googleLogin");
        registry.addViewController("/admin/products");
        registry.addViewController("/admin/product");
        registry.addViewController("/admin/dishes");
        registry.addViewController("/admin/dish");
        registry.addViewController("/admin/users");
        registry.addViewController("/admin/user");
    }
}
