package edu.geekhub.homework;

import edu.geekhub.homework.security.SecurityUser;
import edu.geekhub.homework.users.Role;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    SimpleUrlAuthenticationSuccessHandler userSuccessHandler =
        new SimpleUrlAuthenticationSuccessHandler("/mainUser");
    SimpleUrlAuthenticationSuccessHandler adminSuccessHandler =
        new SimpleUrlAuthenticationSuccessHandler("/main");

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication
    ) throws IOException, ServletException {

        SecurityUser user = (SecurityUser) authentication.getPrincipal();
        if (user.getAuthorities().contains(Role.SELLER)
            || user.getAuthorities().contains(Role.ADMIN)
            || user.getAuthorities().contains(Role.SUPER_ADMIN)) {
            this.adminSuccessHandler.onAuthenticationSuccess(request, response, authentication);
            return;
        }

        this.userSuccessHandler.onAuthenticationSuccess(request, response, authentication);
    }

}
