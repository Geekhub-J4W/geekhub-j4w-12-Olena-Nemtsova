package edu.geekhub.homework.domain;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class Order {
    private int id;
    private final List<Product> products;
    private final double totalPrice;
    private final LocalDateTime dateTime;

    public Order(List<Product> products, LocalDateTime dateTime) {
        this(-1, products, dateTime);
    }

    protected Order(int id, List<Product> products, LocalDateTime dateTime) {
        this.id = id;
        this.products = products;
        totalPrice = products.stream()
            .mapToDouble(Product::getPrice)
            .sum();
        this.dateTime = dateTime;
    }

    protected void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public List<Product> getOrderProducts() {
        return products;
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

        return "Order{"
            + "id=" + id
            + ", products=" + products
            + ", totalPrice=" + totalPrice
            + ", dateTime=" + dateTime.format(formatter)
            + '}';
    }
}
