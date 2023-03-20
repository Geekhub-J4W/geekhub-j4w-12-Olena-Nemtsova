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
            .addResourceLocations("classpath:/ui/");
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("forward:/login.html");
        registry.addViewController("/mainUser").setViewName("forward:/mainUser.html");
        registry.addViewController("/mainAdmin").setViewName("forward:/main.html");
        registry.addViewController("/register").setViewName("forward:/register.html");
        registry.addViewController("/newProduct").setViewName("forward:/newProduct.html");
        registry.addViewController("/newCategory").setViewName("forward:/newCategory.html");
        registry.addViewController("/newUser").setViewName("forward:/newUser.html");
        registry.addViewController("/newOrder").setViewName("forward:/newOrder.html");
    }
}
