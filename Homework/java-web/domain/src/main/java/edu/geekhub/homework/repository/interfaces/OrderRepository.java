package edu.geekhub.homework.repository.interfaces;

import edu.geekhub.homework.domain.Order;
import edu.geekhub.homework.domain.Product;
import java.util.List;

public interface OrderRepository {

    List<Order> getOrders();

    int addOrder(Order order);

    boolean deleteOrderById(int id);

    Order getOrderById(int id);

    List<Product> getOrderProducts(int id);

    boolean updateOrderPriceById(double newPrice, int id);
}

