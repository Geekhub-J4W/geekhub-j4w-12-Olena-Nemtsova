package edu.geekhub.homework.config;

import edu.geekhub.homework.CustomAuthenticationSuccessHandler;
import edu.geekhub.homework.security.LoginPasswordAuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
public class SecurityConfig {

    @Autowired
    public void configureAuthenticationManager(
        AuthenticationManagerBuilder auth,
        LoginPasswordAuthenticationProvider loginPasswordAuthenticationProvider
    ) {
        auth.authenticationProvider(loginPasswordAuthenticationProvider);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .authorizeHttpRequests()
            .requestMatchers(
                "/login", "/error", "/mainUser",
                "/checkout", "/product", "/register",
                "/js/**", "/products/**", "/categories/**",
                "users/**", "orders/**", "reviews/**"
            ).permitAll()

            .requestMatchers(
                "/main", "/newCategory",
                "/newProduct", "/newOrder"
            ).hasAnyAuthority("SELLER", "ADMIN", "SUPER_ADMIN")

            .requestMatchers("/newUser").hasAnyAuthority("ADMIN", "SUPER_ADMIN")

            .anyRequest().authenticated()
            .and()
            .formLogin()
            .loginPage("/login")
            .usernameParameter("username")
            .passwordParameter("password")
            .permitAll()
            .successHandler(new CustomAuthenticationSuccessHandler())
            .and()
            .oauth2Login()
            .loginPage("/login")
            .defaultSuccessUrl("/googleLogin")
            .and()
            .logout()
            .logoutUrl("/logout")
            .logoutSuccessUrl("/login?logout").permitAll()
            .deleteCookies("JSESSIONID")
            .and()
            .rememberMe()
            .key("uniqueAndSecret");

        return http.build();
    }
}
