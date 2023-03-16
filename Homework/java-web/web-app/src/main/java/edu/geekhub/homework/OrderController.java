package edu.geekhub.homework;

import edu.geekhub.homework.domain.Order;
import edu.geekhub.homework.domain.Product;
import edu.geekhub.homework.domain.User;
import edu.geekhub.homework.service.interfaces.OrderService;
import java.util.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public Collection<Order> getOrders() {
        return orderService.getOrders();
    }

    @GetMapping("/{id}")
    public Order getOrderById(@PathVariable int id) {
        return orderService.getOrderById(id);
    }

    @GetMapping("/products/{id}")
    public Collection<Product> getOrderProducts(@PathVariable(value = "id") int id) {
        return orderService.getOrderProducts(id);
    }

    @GetMapping("/customer/{id}")
    public User getOrderCustomer(@PathVariable(value = "id") int id) {
        return orderService.getOrderCustomer(id);
    }

    @PostMapping("/editOrder/{id}")
    public Order editOrderStatus(@PathVariable(value = "id") int id,
                                 @RequestBody Order order) {

        Order updatedOrder = orderService.updateOrderStatusById(order.getStatus(), id);
        if (updatedOrder == null) {
            throw new IllegalArgumentException("Order wasn't updated");
        }
        return updatedOrder;
    }
}
