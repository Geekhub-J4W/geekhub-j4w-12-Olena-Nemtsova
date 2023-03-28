package edu.geekhub.homework.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class Order {
    private int id;
    @JsonFormat(shape = JsonFormat.Shape.OBJECT, pattern = "dd.MM.yyyy HH:mm")
    private LocalDateTime dateTime;
    private double totalPrice;
    private String userId;
    private OrderStatus status;

    public Order(int id, LocalDateTime dateTime, double totalPrice,
                 String userId, OrderStatus status) {
        this.id = id;
        this.dateTime = dateTime;
        this.totalPrice = totalPrice;
        this.userId = userId;
        this.status = status;
    }

    public Order() {
        this(-1, null, 0, null, OrderStatus.PENDING);
    }

    public Order(LocalDateTime dateTime, String userId) {
        this(-1, dateTime, 0, userId, OrderStatus.PENDING);
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String createCheck(List<Product> products) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

        return "Order #" + id
               + "\nProducts:\n"
               + products.toString()
                   .replace("[", "")
                   .replace(", ", "\n")
                   .replace("]", "")
               + "\ntotalPrice: " + totalPrice
               + "\ndate: " + dateTime.format(formatter);
    }
}
