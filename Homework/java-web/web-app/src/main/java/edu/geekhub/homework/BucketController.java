package edu.geekhub.homework;

import edu.geekhub.homework.domain.User;
import edu.geekhub.homework.service.BucketService;
import edu.geekhub.homework.service.interfaces.CategoryService;
import edu.geekhub.homework.service.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class BucketController {
    private final BucketService bucketService;
    private final CategoryService categoryService;
    private final UserService userService;

    @Autowired
    public BucketController(BucketService bucketService,
                            CategoryService categoryService,
                            UserService userService) {
        this.bucketService = bucketService;
        this.categoryService = categoryService;
        this.userService = userService;
    }

    @GetMapping("/bucket/add/{userId}/{productId}")
    public String addProduct(@PathVariable(value = "productId") int productId,
                             @PathVariable(value = "userId") String userId) {
        User user = userService.getUserById(userId);
        if (user == null || user.isAdmin()) {
            return "redirect:/";
        }
        bucketService.addProductById(productId, userId);
        return "redirect:/mainUser/" + userId;
    }

    @GetMapping("/bucket/delete/{userId}/{productId}")
    public String deleteProduct(@PathVariable(value = "productId") int productId,
                                @PathVariable(value = "userId") String userId) {
        User user = userService.getUserById(userId);
        if (user == null || user.isAdmin()) {
            return "redirect:/";
        }
        bucketService.deleteProduct(productId, userId);
        return "redirect:/bucket/" + userId;
    }

    @GetMapping("/bucket/{userId}")
    public String bucketProducts(@PathVariable(value = "userId") String userId, Model model) {
        User user = userService.getUserById(userId);
        if (user == null || user.isAdmin()) {
            return "redirect:/";
        }
        model.addAttribute("totalPrice", bucketService.getBucketTotalPrice(userId));
        model.addAttribute("products", bucketService.getBucketProducts(userId));
        model.addAttribute("categories", categoryService.getCategories());
        model.addAttribute("userId", userId);
        return "/bucket";
    }

    @GetMapping("/bucket/checkout/{userId}")
    public String checkout(@PathVariable(value = "userId") String userId, Model model) {
        User user = userService.getUserById(userId);
        if (user == null || user.isAdmin()) {
            return "redirect:/";
        }
        String checkContent = bucketService.checkout(userId);
        model.addAttribute("checkContent", checkContent);
        model.addAttribute("userId", userId);
        model.addAttribute("products", bucketService.getBucketProducts(userId));
        model.addAttribute("categories", categoryService.getCategories());
        return "/checkout";
    }
}
