package edu.geekhub.homework;

import edu.geekhub.homework.domain.Product;
import edu.geekhub.homework.domain.ProductsSortType;
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
public class ProductController {

    private final ProductService productService;
    private final CategoryService categoryService;
    private final UserService userService;

    @Autowired
    public ProductController(ProductService productService,
                             CategoryService categoryService,
                             UserService userService) {
        this.productService = productService;
        this.categoryService = categoryService;
        this.userService = userService;
    }


    @GetMapping("/products/newProduct/{userId}")
    public String newProduct(Model model, @PathVariable(value = "userId") String userId) {
        User user = userService.getUserById(userId);
        if (user == null || !user.isAdmin()) {
            return "redirect:/";
        }
        model.addAttribute("product", new Product());
        model.addAttribute("categories", categoryService.getCategories());
        model.addAttribute("userId", userId);
        return "/newProduct";
    }

    @PostMapping("/products/newProduct/{userId}")
    public String saveProduct(@ModelAttribute Product product,
                              @PathVariable(value = "userId") String userId,
                              Model model) {
        User user = userService.getUserById(userId);
        if (user == null || !user.isAdmin()) {
            return "redirect:/";
        }
        if (product.getId() != -1) {
            if (!productService.updateProductById(product, product.getId())) {
                model.addAttribute("message", "Product wasn't updated");
                return "error";
            }
        } else {
            if (!productService.addProduct(product)) {
                model.addAttribute("message", "Product wasn't added");
                return "error";
            }
        }
        return "redirect:/mainAdmin/" + userId;
    }

    @PostMapping(value = "/products/deleteProduct/{id}/{userId}")
    public String deleteProduct(@PathVariable(value = "id") int id,
                                @PathVariable(value = "userId") String userId,
                                Model model) {
        User user = userService.getUserById(userId);
        if (user == null || !user.isAdmin()) {
            return "redirect:/";
        }

        if (!productService.deleteProductById(id)) {
            model.addAttribute("message", "Product wasn't deleted");
            return "error";
        }
        return "redirect:/mainAdmin/" + userId;
    }

    @GetMapping("/products/editProduct/{id}/{userId}")
    public String editProduct(@PathVariable(value = "id") int id, Model model,
                              @PathVariable(value = "userId") String userId) {
        User user = userService.getUserById(userId);
        if (user == null || !user.isAdmin()) {
            return "redirect:/";
        }
        model.addAttribute("product", productService.getProductById(id));
        model.addAttribute("categories", categoryService.getCategories());
        model.addAttribute("userId", userId);
        return "/newProduct";
    }

    @GetMapping("/mainUser/{userId}/{id}")
    public String productsByCategory(@PathVariable(value = "userId") String userId,
                                     @PathVariable(value = "id") int id,
                                     Model model) {
        User user = userService.getUserById(userId);
        if (user == null || user.isAdmin()) {
            return "redirect:/";
        }
        var products = productService.getSortedProducts(ProductsSortType.RATING, id);
        model.addAttribute("products", products);
        model.addAttribute("categories", categoryService.getCategories());
        model.addAttribute("title", categoryService.getCategoryById(id).getName());
        model.addAttribute("categoryId", id);
        model.addAttribute("userId", userId);
        return "/mainUser";
    }

    @GetMapping("/mainUser/{sortType}/{categoryId}/{userId}")
    public String productsSorted(@PathVariable(value = "userId") String userId,
                                 @PathVariable(value = "categoryId") int categoryId,
                                 @PathVariable(value = "sortType") String sortType,
                                 Model model) {
        User user = userService.getUserById(userId);
        if (user == null || user.isAdmin()) {
            return "redirect:/";
        }

        String categoryName = categoryService.getCategoryById(categoryId) == null
            ? "All products" : categoryService.getCategoryById(categoryId).getName();

        model.addAttribute("products", productService
            .getSortedProducts(ProductsSortType.valueOf(sortType), categoryId));
        model.addAttribute("categories", categoryService.getCategories());
        model.addAttribute("title", categoryName);
        model.addAttribute("categoryId", categoryId);
        model.addAttribute("userId", userId);
        return "/mainUser";
    }
}
