package edu.geekhub.homework.productsorders;

public record ProductOrder(int id, int productId, int orderId) {
    public ProductOrder(int productId, int orderId) {
        this(-1, productId, orderId);
    }
}
