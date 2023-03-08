package edu.geekhub.homework.domain;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class Order {
    private int id;
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

    public void setStatus(String status) {
        this.status = OrderStatus.valueOf(status);
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

    public String getDateTimeString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

        return dateTime.format(formatter);
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public void setDateTime(String dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        this.dateTime = LocalDateTime.parse(dateTime, formatter);
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
