package edu.geekhub.homework.orders;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;

public class Order {
    private int id;
    @JsonFormat(shape = JsonFormat.Shape.OBJECT, pattern = "dd.MM.yyyy HH:mm")
    private LocalDateTime dateTime;
    private double totalPrice;
    private int userId;
    private OrderStatus status;
    private String address;
    private String customerName;

    public Order(int id, LocalDateTime dateTime, double totalPrice,
                 int userId, OrderStatus status, String address, String customerName) {
        this.id = id;
        this.dateTime = dateTime;
        this.totalPrice = totalPrice;
        this.userId = userId;
        this.status = status;
        this.address = address;
        this.customerName = customerName;
    }

    public Order() {
        this(-1, null, 0, -1, OrderStatus.PENDING, null, null);
    }

    public Order(LocalDateTime dateTime, int userId, String address) {
        this(-1, dateTime, 0, userId, OrderStatus.PENDING, address, null);
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

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

}
