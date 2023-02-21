package edu.geekhub.homework.domain;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.List;
import org.tinylog.Logger;

public class OrderRepository {
    private final List<Order> orders;
    private final File ordersFile;
    private int counter = 0;

    public OrderRepository(List<Order> orders, File ordersFile) {
        this.ordersFile = ordersFile;
        this.orders = orders;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public boolean addOrder(Order order) {
        if (order.getOrderProducts().isEmpty()) {
            Logger.warn("Can't add empty order");
            return false;
        }
        if (order.getId() == -1) {
            order.setId(++counter);
        } else {
            counter = order.getId();
        }
        saveToFile(order);
        orders.add(order);
        Logger.info("Order was added:\n" + order);
        return true;
    }

    public boolean saveToFile(Order order) {
        if (order.getId() == -1) {
            Logger.warn("Can't save order with id -1 to file");
            return false;
        }

        try (FileOutputStream fos = new FileOutputStream(ordersFile, false);
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
