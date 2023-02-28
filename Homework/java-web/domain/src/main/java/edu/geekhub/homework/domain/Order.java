package edu.geekhub.homework.domain;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public record Order(int id, LocalDateTime dateTime, double totalPrice) {

    public Order(LocalDateTime dateTime) {
        this(-1, dateTime, 0);
    }

    public Order changeId(int id) {
        return new Order(id, dateTime(), totalPrice());
    }

    public Order changeTotalPrice(double totalPrice) {
        return new Order(id(), dateTime(), totalPrice);
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

        return "Order{"
               + "id=" + id
               + ", totalPrice=" + totalPrice
               + ", dateTime=" + dateTime.format(formatter)
               + '}';
    }
}
