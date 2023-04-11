package edu.geekhub.homework;

import edu.geekhub.homework.security.SecurityUser;
import edu.geekhub.homework.users.Role;
import edu.geekhub.homework.users.User;
import edu.geekhub.homework.users.interfaces.UserService;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.AccessException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
        this.passwordEncoder = new BCryptPasswordEncoder(12);
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN')")
    public Collection<User> getAllUsers() {
        return userService.getUsers();
    }

    @GetMapping("/{userId}")
    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN', 'ADMIN', 'SELLER')")
    public User getUserById(@PathVariable int userId) {
        User srchUser = userService.getUserById(userId);
        if (srchUser == null) {
            throw new IllegalArgumentException("User not exists by id: " + userId);
        }
        return srchUser;
    }


    @GetMapping("/info")
    public User getUser() {
        int userId = getUserId();
        if (userId == -1) {
            return null;
        }
        User srchUser = userService.getUserById(userId);
        if (srchUser == null) {
            throw new IllegalArgumentException("User not exists by id: " + userId);
        }
        return srchUser;
    }

    @GetMapping("/role/{role}")
    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN', 'ADMIN')")
    public Collection<User> getUsersByRole(@PathVariable String role) {
        return userService.getUsersByRole(Role.valueOf(role));
    }

    @DeleteMapping("/{userId}")
    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN', 'ADMIN')")
    public void deleteUser(@PathVariable int userId) throws AccessException {
        User currentUser = userService.getUserById(getUserId());
        User userToDelete = userService.getUserById(userId);
        if (userToDelete.getRole() == Role.ADMIN
            && currentUser.getRole() == Role.ADMIN
            || userToDelete.getRole() == Role.SUPER_ADMIN) {
            throw new AccessException("User wasn't deleted");
        }

        if (!userService.deleteUserById(userId)) {
            throw new IllegalArgumentException("User wasn't deleted");
        }
    }

    @PostMapping("/newTemp")
    public User addTempUser(@RequestBody User user) {
        User existsUser = userService.getUserByEmail(user.getEmail());
        if (existsUser != null) {
            return existsUser;
        }

        user.setPassword("Temporary1");
        user.setRole(Role.USER);

        return userService.addUser(user);
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN', 'ADMIN')")
    public User addUser(@RequestBody User user) throws AccessException {
        User currentUser = userService.getUserById(getUserId());
        if (user.getRole() == Role.ADMIN
            && currentUser.getRole() == Role.ADMIN
            || user.getRole() == Role.SUPER_ADMIN) {
            throw new AccessException("User wasn't added");
        }
        return userService.addUser(user);
    }

    @PostMapping("/new")
    public User registerUser(@RequestBody User user, HttpServletRequest httpServletRequest) {
        User existsUser = userService.getUserByEmail(user.getEmail());
        user.setRole(Role.USER);
        if (existsUser != null
            && passwordEncoder.matches("Temporary1", existsUser.getPassword())) {

            User updatedUser = userService.updateUserById(user, existsUser.getId());
            if (updatedUser == null) {
                throw new IllegalArgumentException("Temp user wasn't updated to user");
            }
            authWithoutPassword(updatedUser, httpServletRequest);
            return updatedUser;
        }

        User newUser = userService.addUser(user);
        if (newUser == null) {
            throw new IllegalArgumentException("User wasn't added");
        }
        authWithoutPassword(newUser, httpServletRequest);
        return newUser;
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

    @PostMapping("/edit")
    public User editCurrentUser(@RequestBody User user) {
        User userToEdit = userService.getUserById(getUserId());
        user.setRole(userToEdit.getRole());

        User updatedUser = userService.updateUserById(user, getUserId());
        if (updatedUser == null) {
            throw new IllegalArgumentException("User wasn't updated");
        }
        return updatedUser;
    }

    @PostMapping("/{userId}")
    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN', 'ADMIN')")
    public User editUserById(@RequestBody User user,
                             @PathVariable int userId) throws AccessException {

        User currentUser = userService.getUserById(getUserId());
        User userToUpdate = userService.getUserById(userId);
        if (user.getRole() == Role.ADMIN
            && currentUser.getRole() == Role.ADMIN
            || user.getRole() == Role.SUPER_ADMIN
            || userToUpdate == null
            || userToUpdate.getRole() == Role.SUPER_ADMIN) {
            throw new AccessException("User wasn't added");
        }

        User updatedUser = userService.updateUserById(user, userId);
        if (updatedUser == null) {
            throw new IllegalArgumentException("User wasn't updated");
        }
        return updatedUser;
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
}
