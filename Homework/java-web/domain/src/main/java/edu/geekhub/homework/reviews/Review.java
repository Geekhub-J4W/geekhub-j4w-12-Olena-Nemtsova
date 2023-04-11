package edu.geekhub.homework.reviews;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Review {
    private int id;
    @JsonFormat(shape = JsonFormat.Shape.OBJECT, pattern = "dd.MM.yyyy HH:mm")
    private LocalDateTime dateTime;
    private String text;
    private int rating;
    private int productId;
    private int orderId;

    public Review(int id,
                  LocalDateTime dateTime,
                  String text,
                  int rating,
                  int productId,
                  int orderId) {
        this.id = id;
        this.dateTime = dateTime;
        this.text = text;
        this.rating = rating;
        this.productId = productId;
        this.orderId = orderId;
    }

    public Review() {
        this(-1, null, null, 0, -1, -1);
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

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

        return "Review{"
               + "id=" + id
               + ", dateTime=" + dateTime.format(formatter)
               + ", text='" + text + '\''
               + ", rating=" + rating
               + ", productId=" + productId
               + ", orderId=" + orderId
               + '}';
    }
}
