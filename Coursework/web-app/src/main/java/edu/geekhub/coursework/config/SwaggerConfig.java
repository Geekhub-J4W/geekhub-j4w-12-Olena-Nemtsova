package edu.geekhub.coursework.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI customOpenApi(Info info) {
        return new OpenAPI()
            .info(info);
    }

    @Bean
    public Info info(@Value("${openapi.project-title}") String title,
                     @Value("${openapi.project-version}") String version,
                     @Value("${openapi.project-description}") String description,
                     Contact contact) {
        return new Info()
            .title(title)
            .version(version)
            .description(description)
            .contact(contact);
    }

    @Bean
    public Contact contact() {
        return new Contact()
            .name("Lena")
            .email("assassin1me1mow@gmail.com");
    }
}
