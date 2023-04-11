package edu.geekhub.homework.orders.interfaces;

import edu.geekhub.homework.orders.Order;
import edu.geekhub.homework.products.Product;
import java.util.List;

public interface OrderRepository {

    List<Order> getOrders();

    List<Order> getOrdersByUserId(int userId);

    int addOrder(Order order);

    void deleteOrderById(int id);

    Order getOrderById(int id);

    List<Product> getOrderProducts(int id);

    void updateOrderPriceById(double newPrice, int id);

    void updateOrderById(Order order, int id);

    int getQuantityOfProductAtOrder(int productId, int orderId);
}

