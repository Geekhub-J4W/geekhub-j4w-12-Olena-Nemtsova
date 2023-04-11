package edu.geekhub.homework.orders;

import edu.geekhub.homework.users.interfaces.UserRepository;
import java.time.LocalDateTime;
import org.springframework.stereotype.Component;

@Component
public class OrderValidator {
    private final UserRepository userRepository;

    public OrderValidator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void validate(Order order) {
        if (order == null) {
            throw new IllegalArgumentException("Order was null");
        }
        validateDateTime(order.getDateTime());
        validateTotalPrice(order.getTotalPrice());
        validateUserId(order.getUserId());
        validateAddress(order.getAddress());
        validateCustomerName(order.getCustomerName());
        validateStatus(order.getStatus());
    }

    private void validateStatus(OrderStatus status) {
        if (status == null) {
            throw new IllegalArgumentException("Order status was null");
        }
    }

    private void validateCustomerName(String customerName) {
        if (customerName == null || customerName.isBlank()) {
            throw new IllegalArgumentException("Order customer name was null or empty");
        }
        if (customerName.split(" ").length < 2) {
            throw new IllegalArgumentException("Order customer name contained less than 2 words");
        }
    }

    private void validateAddress(String address) {
        if (address == null || address.isBlank()) {
            throw new IllegalArgumentException("Order address was null or empty");
        }
    }

    private void validateUserId(int userId) {
        if (userRepository.getUserById(userId) == null) {
            throw new IllegalArgumentException("Order had not exists user id");
        }

    }

    public void validateTotalPrice(double totalPrice) {
        if (totalPrice < 0) {
            throw new IllegalArgumentException("Order total price was less than 0");
        }
        String numbersAfterPoint = Double.toString(totalPrice).split("\\.")[1];
        if (numbersAfterPoint.length() > 2) {
            throw new IllegalArgumentException(
                "Order total price had too much numbers after point"
            );
        }
    }

    private void validateDateTime(LocalDateTime dateTime) {
        if (dateTime == null) {
            throw new IllegalArgumentException("Order dateTime was null");
        }
        if (dateTime.isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException("Order dateTime was more than now");
        }
    }
}
