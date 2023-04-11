package edu.geekhub.homework.orders.interfaces;

import edu.geekhub.homework.orders.Order;
import edu.geekhub.homework.products.Product;
import java.util.List;

public interface OrderService {

    List<Order> getOrders();

    List<Order> getOrdersByUserId(int userId);

    Order addOrder(Order order);

    boolean deleteOrder(int id);

    Order getOrderById(int id);

    List<Product> getOrderProducts(int id);

    boolean updateOrderPriceById(double newPrice, int id);

    Order updateOrderById(Order order, int id);

    int getQuantityOfProductAtOrder(int productId, int orderId);

    boolean addProductsOfOrder(List<Product> products, int id);
}
