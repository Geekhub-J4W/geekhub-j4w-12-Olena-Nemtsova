package edu.geekhub.homework;

import edu.geekhub.homework.domain.Order;
import edu.geekhub.homework.domain.OrderStatus;
import edu.geekhub.homework.domain.Product;
import edu.geekhub.homework.domain.User;
import edu.geekhub.homework.service.interfaces.OrderService;
import edu.geekhub.homework.service.interfaces.UserService;
import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class OrderController {
    private final OrderService orderService;
    private final UserService userService;

    @Autowired
    public OrderController(OrderService orderService, UserService userService) {
        this.orderService = orderService;
        this.userService = userService;
    }


    @GetMapping("/orders/{userId}")
    public String orders(Model model, @PathVariable(value = "userId") String userId) {
        User user = userService.getUserById(userId);
        if (user == null || !user.isAdmin()) {
            return "redirect:/";
        }
        model.addAttribute("userId", userId);
        model.addAttribute("orders", orderService.getOrders());
        model.addAttribute("products", new ArrayList<Product>());
        model.addAttribute("orderId", -1);
        model.addAttribute("user", new User());
        return "/orders";
    }

    @GetMapping("/orders/details/{id}/{userId}")
    public String orderDetails(Model model,
                               @PathVariable(value = "userId") String userId,
                               @PathVariable(value = "id") int id) {
        User user = userService.getUserById(userId);
        if (user == null || !user.isAdmin()) {
            return "redirect:/";
        }
        model.addAttribute("userId", userId);
        model.addAttribute("orders", orderService.getOrders());
        model.addAttribute("products", orderService.getOrderProducts(id));
        model.addAttribute("orderId", id);
        model.addAttribute("user", orderService.getOrderCustomer(id));
        return "/orders";
    }

    @GetMapping("/orders/editOrder/{id}/{userId}")
    public String editOrder(@PathVariable(value = "id") int id,
                            @PathVariable(value = "userId") String userId,
                            Model model) {
        User user = userService.getUserById(userId);
        if (user == null || !user.isAdmin()) {
            return "redirect:/";
        }
        model.addAttribute("order", orderService.getOrderById(id));
        model.addAttribute("userId", userId);
        model.addAttribute("orderStatuses", OrderStatus.values());
        return "/newOrder";
    }

    @PostMapping("/orders/newOrder/{userId}")
    public String saveOrder(@ModelAttribute Order order,
                            @PathVariable(value = "userId") String userId,
                            Model model) {
        User user = userService.getUserById(userId);
        if (user == null || !user.isAdmin()) {
            return "redirect:/";
        }
        if (!orderService.updateOrderStatusById(order.getStatus(), order.getId())) {
            model.addAttribute("message", "Order wasn't updated");
            return "error";
        }

        return "redirect:/orders/" + userId;
    }
}
