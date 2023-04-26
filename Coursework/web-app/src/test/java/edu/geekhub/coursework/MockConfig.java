package edu.geekhub.coursework;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

import edu.geekhub.coursework.security.SecurityUser;
import edu.geekhub.coursework.security.UserDetailsServiceImpl;
import edu.geekhub.coursework.users.Role;
import edu.geekhub.coursework.users.User;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.jwt.JwtDecoder;

@Configuration
public class MockConfig {
    @Bean
    public JwtDecoder jwtDecoder() {
        return mock(JwtDecoder.class);
    }

    @Bean
    public UserDetailsService userDetailsService() {
        UserDetailsService userDetailsService = mock(UserDetailsServiceImpl.class);
        User user = new User(1, "Mark", "Pearce", "Qwerty1", "user@gmail.com", Role.USER);
        User admin = new User(2, "Mark", "Pearce", "Qwerty1", "admin@gmail.com", Role.ADMIN);

        doReturn(new SecurityUser(user))
            .when(userDetailsService).loadUserByUsername("user@gmail.com");
        doReturn(new SecurityUser(admin))
            .when(userDetailsService).loadUserByUsername("admin@gmail.com");

        return userDetailsService;
    }
}
