package edu.geekhub.homework.service;

import edu.geekhub.homework.domain.Order;
import edu.geekhub.homework.domain.Product;
import edu.geekhub.homework.repository.interfaces.OrderRepository;
import edu.geekhub.homework.service.interfaces.OrderService;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.List;
import org.tinylog.Logger;

public class OrderServiceImpl implements OrderService {
    private final File ordersFile;
    private final OrderRepository orderRepository;

    public OrderServiceImpl(OrderRepository orderRepository, File ordersFile) {
        this.orderRepository = orderRepository;
        this.ordersFile = ordersFile;
    }

    @Override
    public List<Order> getOrders() {
        return orderRepository.getOrders();
    }

    @Override
    public int addOrder(Order order) {
        return orderRepository.addOrder(order);
    }

    @Override
    public boolean deleteOrder(int id) {
        if (orderRepository.deleteOrderById(id)) {
            Logger.info("Order with id '" + id + "' was deleted from repository");
            return true;
        }
        Logger.warn("Order with id '" + id + "' wasn't deleted from repository: not found");
        return false;
    }

    @Override
    public Order getOrderById(int id) {
        Order order = orderRepository.getOrderById(id);
        if (order == null) {
            Logger.warn("Not found order with id " + id);
        }
        return order;
    }

    @Override
    public List<Product> getOrderProducts(int id) {
        return orderRepository.getOrderProducts(id);
    }

    @Override
    public boolean updateOrderPriceById(double newPrice, int id) {
        Order orderToEdit = getOrderById(id);
        if (orderToEdit == null) {
            Logger.warn("Order price wasn't updated: not found order with id " + id);
            return false;
        }
        orderRepository.updateOrderPriceById(newPrice, id);
        Logger.info("Order price was updated: "
                    + orderToEdit + "\n"
                    + "new price: " + newPrice);
        return true;
    }

    @Override
    public boolean saveToFile(Order order) {
        if (order.id() == -1) {
            Logger.warn("Can't save order with id -1 to file");
            return false;
        }

        try (FileOutputStream fos = new FileOutputStream(ordersFile, true);
             PrintStream printStream = new PrintStream(fos)) {

            printStream.println(order);
            Logger.info("Check of order was saved to file:\n" + this);
            return true;
        } catch (IOException ex) {
            Logger.warn(ex.getMessage());
            return false;
        }
    }
}
