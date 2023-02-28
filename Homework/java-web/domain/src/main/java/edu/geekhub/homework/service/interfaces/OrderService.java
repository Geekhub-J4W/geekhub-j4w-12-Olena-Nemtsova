package edu.geekhub.homework.service.interfaces;

import edu.geekhub.homework.domain.Order;
import edu.geekhub.homework.domain.Product;
import java.util.List;

public interface OrderService {

    List<Order> getOrders();

    int addOrder(Order order);

    boolean deleteOrder(int id);

    Order getOrderById(int id);

    List<Product> getOrderProducts(int id);

    boolean updateOrderPriceById(double newPrice, int id);

    boolean saveToFile(Order order);
}
