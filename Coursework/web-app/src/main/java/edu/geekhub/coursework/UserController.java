package edu.geekhub.coursework;

import edu.geekhub.coursework.security.SecurityUser;
import edu.geekhub.coursework.users.Role;
import edu.geekhub.coursework.users.User;
import edu.geekhub.coursework.users.interfaces.UserService;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Collection;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.tinylog.Logger;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN', 'ADMIN', 'USER')")
    public User getUser() {
        return userService.getUserById(getUserId());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN', 'ADMIN')")
    public User getUserById(@PathVariable int id) {
        return userService.getUserById(id);
    }

    @GetMapping("/all/{role}")
    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN', 'ADMIN')")
    public Collection<User> getUsersByRole(@PathVariable String role) {
        return userService.getUsersByRole(Role.valueOf(role));
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN', 'ADMIN')")
    public User addUser(@RequestBody User user) {
        return userService.addUser(user);
    }

    @PostMapping("/register")
    public User registerUser(@RequestBody User user, HttpServletRequest request) {
        user.setRole(Role.USER);
        User newUser = userService.addUser(user);
        if (newUser != null) {
            authWithoutPassword(newUser, request);
        }
        return newUser;
    }

    @PostMapping("/google")
    public String googleRegister(
        @AuthenticationPrincipal OAuth2User user,
        HttpServletRequest request
    ) {
        String email = user.getAttribute("email");

        User existsUser = userService.getUserByEmail(email);
        if (existsUser == null) {
            String lastName = user.getAttribute("family_name");
            String firstName = user.getAttribute("given_name");

            User newUser = new User(
                -1,
                firstName,
                lastName,
                "Temporary1",
                email,
                Role.USER
            );
            existsUser = userService.addUser(newUser);
        }

        authWithoutPassword(existsUser, request);
        if (existsUser.getRole() == Role.USER) {
            return "/mainUser";
        } else {
            return "/mainAdmin";
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN', 'ADMIN')")
    public boolean deleteUserById(@PathVariable int id) {
        User userToDelete = userService.getUserById(id);

        if (userRoleSameToCurrent(userToDelete)) {
            Logger.warn("Delete operation not supported");
            return false;
        }
        return userService.deleteUserById(id);
    }

    @PutMapping
    public User updateUser(@RequestBody User user) {
        user.setRole(Role.USER);
        return userService.updateUserById(user, getUserId());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN', 'ADMIN')")
    public User updateUserById(
        @RequestBody User user,
        @PathVariable int id
    ) {
        User userUpdate = userService.getUserById(id);

        if (userRoleSameToCurrent(user)
            || userRoleSameToCurrent(userUpdate)) {
            Logger.warn("Update operation not supported");
            return null;
        }

        return userService.updateUserById(user, id);
    }

    public boolean userRoleSameToCurrent(User user) {
        User currentUser = userService.getUserById(getUserId());
        return currentUser != null
               && user != null
               && currentUser.getRole() == user.getRole();
    }

    private int getUserId() {
        Object object = SecurityContextHolder
            .getContext()
            .getAuthentication()
            .getPrincipal();

        if (object.toString().equals("anonymousUser")) {
            return -1;
        }
        SecurityUser user = (SecurityUser) object;
        return user.getUserId();
    }

    public void authWithoutPassword(User user, HttpServletRequest request) {
        SecurityUser securityUser = new SecurityUser(user);

        Authentication authentication = new UsernamePasswordAuthenticationToken(
            securityUser,
            null,
            securityUser.getAuthorities()
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        request.getSession().setAttribute(
            HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
            SecurityContextHolder.getContext()
        );
    }
}
