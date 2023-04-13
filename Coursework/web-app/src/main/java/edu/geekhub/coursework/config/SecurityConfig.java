package edu.geekhub.coursework.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class SecurityConfig {

    @Autowired
    public void configureGlobalSecurity(
        AuthenticationManagerBuilder auth,
        UserDetailsService userDetailsService
    ) throws Exception {

        auth.userDetailsService(userDetailsService)
            .passwordEncoder(new BCryptPasswordEncoder(12));
    }
}
