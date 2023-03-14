package edu.geekhub.homework;

import edu.geekhub.homework.domain.User;
import edu.geekhub.homework.service.interfaces.UserService;
import java.util.Collection;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public Collection<User> getAllUsers() {
        return userService.getUsers();
    }

    @GetMapping("/{email}/{pass}")
    public User getUserByEmailAndPass(@PathVariable String email, @PathVariable String pass) {
        User srchUser = userService.getUserByEmailAndPassword(email, pass);
        if (srchUser == null) {
            throw new IllegalArgumentException("User not exists: " + email + ", " + pass);
        }
        return srchUser;
    }

    @GetMapping("/admins")
    public Collection<User> getOnlyAdmins() {
        return userService.getAdmins();
    }

    @GetMapping("/customers")
    public Collection<User> getOnlyCustomers() {

        return userService.getCustomers();
    }

    @DeleteMapping(value = "/deleteUser/{id}")
    public void deleteUser(@PathVariable(value = "id") String id) {
        if (!userService.deleteUserById(id)) {
            throw new IllegalArgumentException("User wasn't deleted");
        }
    }

    @PostMapping("/newUser")
    public User addUser(@RequestBody User user) {
        User newUser = userService.addUser(user);

        if (newUser == null) {
            throw new IllegalArgumentException("User wasn't added");
        }
        return newUser;
    }

    @PostMapping("editUser/{id}")
    public User editUser(@RequestBody User user,
                         @PathVariable(value = "id") String id) {

        User updatedUser = userService.updateUserById(user, id);
        if (updatedUser == null) {
            throw new IllegalArgumentException("User wasn't updated");
        }
        return updatedUser;
    }
}
