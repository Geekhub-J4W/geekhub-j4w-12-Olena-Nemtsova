package edu.geekhub.homework;

import edu.geekhub.homework.domain.Category;
import edu.geekhub.homework.domain.User;
import edu.geekhub.homework.service.interfaces.CategoryService;
import edu.geekhub.homework.service.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class CategoryController {
    private final CategoryService categoryService;
    private final UserService userService;

    @Autowired
    public CategoryController(CategoryService categoryService, UserService userService) {
        this.categoryService = categoryService;
        this.userService = userService;
    }

    @GetMapping("/categories/{userId}")
    public String allCategories(Model model, @PathVariable(value = "userId") String userId) {
        User user = userService.getUserById(userId);
        if (user == null || !user.isAdmin()) {
            return "redirect:/";
        }
        model.addAttribute("categories", categoryService.getCategories());
        model.addAttribute("userId", userId);
        return "/categories";
    }

    @GetMapping("/categories/newCategory/{userId}")
    public String newCategory(Model model, @PathVariable(value = "userId") String userId) {
        User user = userService.getUserById(userId);
        if (user == null || !user.isAdmin()) {
            return "redirect:/";
        }
        model.addAttribute("category", new Category());
        model.addAttribute("userId", userId);
        return "/newCategory";
    }

    @PostMapping("/categories/newCategory/{userId}")
    public String saveCategory(@ModelAttribute Category category,
                               @PathVariable(value = "userId") String userId,
                               Model model) {
        User user = userService.getUserById(userId);
        if (user == null || !user.isAdmin()) {
            return "redirect:/";
        }
        if (category.getId() != -1) {
            if (!categoryService.updateCategoryById(category, category.getId())) {
                model.addAttribute("message", "Category wasn't updated");
                return "error";
            }
        } else {
            if (!categoryService.addCategory(category)) {
                model.addAttribute("message", "Category wasn't added");
                return "error";
            }
        }
        return "redirect:/categories/" + userId;
    }

    @PostMapping(value = "/categories/deleteCategory/{id}/{userId}")
    public String deleteCategory(@PathVariable(value = "id") int id,
                                 @PathVariable(value = "userId") String userId,
                                 Model model) {
        User user = userService.getUserById(userId);
        if (user == null || !user.isAdmin()) {
            return "redirect:/";
        }
        if (!categoryService.deleteCategoryById(id)) {
            model.addAttribute("message", "Category wasn't deleted");
            return "error";
        }

        return "redirect:/categories/" + userId;
    }

    @GetMapping("/categories/editCategory/{id}/{userId}")
    public String editCategory(@PathVariable(value = "id") int id,
                               @PathVariable(value = "userId") String userId,
                               Model model) {
        User user = userService.getUserById(userId);
        if (user == null || !user.isAdmin()) {
            return "redirect:/";
        }
        model.addAttribute("category", categoryService.getCategoryById(id));
        model.addAttribute("userId", userId);
        return "/newCategory";
    }
}
