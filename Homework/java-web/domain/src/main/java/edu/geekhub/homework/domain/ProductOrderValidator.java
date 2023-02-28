package edu.geekhub.homework.domain;

import edu.geekhub.homework.repository.interfaces.OrderRepository;
import edu.geekhub.homework.repository.interfaces.ProductRepository;

public class ProductOrderValidator {
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;

    public ProductOrderValidator(ProductRepository productRepository,
                                 OrderRepository orderRepository) {
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
    }

    public void validate(ProductOrder productOrder) {
        if (productOrder == null) {
            throw new IllegalArgumentException("ProductOrder was null");
        }
        validateProductExists(productOrder.productId());
        validateOrderExists(productOrder.orderId());
    }

    private void validateOrderExists(int orderId) {
        if (orderRepository.getOrderById(orderId) == null) {
            throw new IllegalArgumentException("Order with id '" + orderId + "' doesn't exist");
        }
    }

    private void validateProductExists(int productId) {
        if (productRepository.getProductById(productId) == null) {
            throw new IllegalArgumentException("Product with id '" + productId + "' doesn't exist");
        }
    }
}
