package edu.geekhub.coursework.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.config.annotation.web.socket.EnableWebSocketSecurity;
import org.springframework.security.messaging.access.intercept.MessageMatcherDelegatingAuthorizationManager;

@Configuration
@EnableWebSocketSecurity
public class WebSocketSecurityConfig {
    @Bean
    AuthorizationManager<Message<?>> messageAuthorizationManager(
        MessageMatcherDelegatingAuthorizationManager.Builder messages
    ) {
        messages
            .anyMessage().authenticated()
            .simpDestMatchers("/app/chat/**").hasAnyAuthority("ADMIN", "SUPER_ADMIN")
            .simpSubscribeDestMatchers("/topic/refresh").hasAnyAuthority("ADMIN", "SUPER_ADMIN");

        return messages.build();
    }
}
