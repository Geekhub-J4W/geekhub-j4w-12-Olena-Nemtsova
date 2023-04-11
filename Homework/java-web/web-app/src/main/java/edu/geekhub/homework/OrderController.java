package edu.geekhub.homework;

import edu.geekhub.homework.orders.Order;
import edu.geekhub.homework.orders.OrderStatus;
import edu.geekhub.homework.orders.interfaces.OrderService;
import edu.geekhub.homework.products.Product;
import edu.geekhub.homework.security.SecurityUser;
import java.util.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
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
    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN', 'ADMIN', 'SELLER')")
    public Collection<Order> getOrders() {
        return orderService.getOrders();
    }

    @GetMapping("/user")
    @PreAuthorize("hasAuthority('USER')")
    public Collection<Order> getUserOrders() {
        return orderService.getOrdersByUserId(getUserId());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN', 'ADMIN', 'SELLER')")
    public Order getOrderById(@PathVariable int id) {
        return orderService.getOrderById(id);
    }

    @GetMapping("/products/{id}")
    public Collection<Product> getOrderProducts(@PathVariable(value = "id") int id) {
        return orderService.getOrderProducts(id);
    }

    @PostMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN', 'ADMIN', 'SELLER')")
    public Order editOrder(@PathVariable(value = "id") int id,
                           @RequestBody Order order) {

        Order updatedOrder = orderService.updateOrderById(order, id);
        if (updatedOrder == null) {
            throw new IllegalArgumentException("Order wasn't updated");
        }
        return updatedOrder;
    }

    @PostMapping("/new")
    public Order newOrder(@RequestBody Order order) {
        order.setStatus(OrderStatus.PENDING);
        Order newOrder = orderService.addOrder(order);
        if (newOrder == null) {
            throw new IllegalArgumentException("Order wasn't added");
        }
        return newOrder;
    }

    @PostMapping("/products/{id}")
    public boolean addOrderProducts(@RequestBody Collection<Product> products,
                                    @PathVariable int id) {
        return orderService.addProductsOfOrder(products.stream().toList(), id);
    }

    @GetMapping("/quantity/{productId}/{orderId}")
    public int getQuantityOfProductAtOrder(@PathVariable int productId,
                                           @PathVariable int orderId) {
        return orderService.getQuantityOfProductAtOrder(productId, orderId);
    }

    private int getUserId() {
        SecurityUser user = (SecurityUser) SecurityContextHolder
            .getContext()
            .getAuthentication()
            .getPrincipal();
        return user.getUserId();
    }
}
