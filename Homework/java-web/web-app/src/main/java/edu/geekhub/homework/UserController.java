package edu.geekhub.homework;

import edu.geekhub.homework.domain.User;
import edu.geekhub.homework.service.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users/{userId}")
    public String users(Model model, @PathVariable(value = "userId") String userId) {
        User user = userService.getUserById(userId);
        if (user == null || !user.isAdmin()) {
            return "redirect:/";
        }
        model.addAttribute("userId", userId);
        model.addAttribute("users", userService.getUsers());
        return "/users";
    }

    @GetMapping("/users/admins/{userId}")
    public String usersAdmins(Model model, @PathVariable(value = "userId") String userId) {
        User user = userService.getUserById(userId);
        if (user == null || !user.isAdmin()) {
            return "redirect:/";
        }
        model.addAttribute("userId", userId);
        model.addAttribute("users", userService.getAdmins());
        return "/users";
    }

    @GetMapping("/users/customers/{userId}")
    public String usersCustomers(Model model, @PathVariable(value = "userId") String userId) {
        User user = userService.getUserById(userId);
        if (user == null || !user.isAdmin()) {
            return "redirect:/";
        }
        model.addAttribute("userId", userId);
        model.addAttribute("users", userService.getCustomers());
        return "/users";
    }

    @PostMapping(value = "/users/deleteUser/{id}/{userId}")
    public String deleteUser(@PathVariable(value = "id") String id,
                             @PathVariable(value = "userId") String userId,
                             Model model) {
        User user = userService.getUserById(userId);
        if (user == null || !user.isAdmin()) {
            return "redirect:/";
        }

        if (!userService.deleteUserById(id)) {
            model.addAttribute("message", "User wasn't deleted");
            return "error";
        }
        return "redirect:/users/" + userId;
    }

    @GetMapping("/users/newAdmin/{userId}")
    public String newUser(Model model, @PathVariable(value = "userId") String userId) {
        User user = userService.getUserById(userId);
        if (user == null || !user.isAdmin()) {
            return "redirect:/";
        }
        model.addAttribute("user", new User());
        model.addAttribute("userId", userId);
        return "/newUser";
    }

    @PostMapping("/users/newUser/{userId}")
    public String saveUser(@ModelAttribute User newUser,
                           @PathVariable(value = "userId") String userId,
                           Model model) {
        User user = userService.getUserById(userId);
        if (user == null || !user.isAdmin()) {
            return "redirect:/";
        }
        if (newUser.getId() != null && !newUser.getId().isBlank()) {
            userService.updateUserById(newUser, newUser.getId());
        } else {
            if (!userService.addUser(newUser)) {
                model.addAttribute("message",
                    "User already exists with email: " + newUser.getEmail());
                return "error";
            }
        }
        return "redirect:/users/" + userId;
    }

    @GetMapping("/users/editUser/{id}/{userId}")
    public String editUser(@PathVariable(value = "id") String id,
                           @PathVariable(value = "userId") String userId,
                           Model model) {
        User user = userService.getUserById(userId);
        if (user == null || !user.isAdmin()) {
            return "redirect:/";
        }
        model.addAttribute("user", userService.getUserById(id));
        model.addAttribute("userId", userId);
        return "/newUser";
    }

    @PostMapping("/register")
    public String saveNewUser(@ModelAttribute User newUser, Model model) {
        if (!userService.addUser(newUser)) {
            model.addAttribute("message",
                "User already exists with email: " + newUser.getEmail());
            return "error";
        }

        return "redirect:/mainUser/" + newUser.getId();
    }
}
