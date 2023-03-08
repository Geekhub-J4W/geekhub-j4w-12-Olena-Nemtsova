package edu.geekhub.homework;

import edu.geekhub.homework.domain.User;
import edu.geekhub.homework.service.interfaces.CategoryService;
import edu.geekhub.homework.service.interfaces.ProductService;
import edu.geekhub.homework.service.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class MainController {
    private final CategoryService categoryService;
    private final ProductService productService;
    private final UserService userService;

    @Autowired
    public MainController(CategoryService categoryService,
                          ProductService productService,
                          UserService userService) {
        this.categoryService = categoryService;
        this.productService = productService;
        this.userService = userService;
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("user", new User());
        return "/login";
    }

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("user", new User());
        return "/register";
    }

    @PostMapping("/main")
    public String login(@ModelAttribute User user) {
        User srchUser = userService.getUserByEmailAndPassword(user.getEmail(), user.getPassword());
        if (srchUser == null) {
            return "redirect:/";
        }
        if (!srchUser.isAdmin()) {
            return "redirect:/mainUser/" + srchUser.getId();
        }

        return "redirect:/mainAdmin/" + srchUser.getId();
    }

    @GetMapping("/mainUser/{userId}")
    public String mainUser(Model model, @PathVariable(value = "userId") String userId) {
        User user = userService.getUserById(userId);
        if (user == null || user.isAdmin()) {
            return "redirect:/";
        }
        model.addAttribute("categories", categoryService.getCategories());
        model.addAttribute("products", productService.getProducts());
        model.addAttribute("title", "All products");
        model.addAttribute("categoryId", -1);
        model.addAttribute("userId", userId);

        return "/mainUser";
    }

    @GetMapping("/mainAdmin/{userId}")
    public String mainAdmin(Model model, @PathVariable(value = "userId") String userId) {
        User user = userService.getUserById(userId);
        if (user == null || !user.isAdmin()) {
            return "redirect:/";
        }
        model.addAttribute("categories", categoryService);
        model.addAttribute("products", productService.getProducts());
        model.addAttribute("userId", userId);

        return "/main";
    }
}
