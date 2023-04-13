package edu.geekhub.coursework.products;

import org.springframework.stereotype.Component;

@Component
public class ProductValidator {
    public void validate(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("Product was null");
        }
        validateName(product.getName());
        validateCalories(product.getCalories());
    }

    private void validateCalories(int calories) {
        if (calories < 0) {
            throw new IllegalArgumentException("Product calories was less than zero");
        }
    }

    private void validateName(String name) {
        if (name == null) {
            throw new IllegalArgumentException("Product name was null");
        }
        if (name.isBlank()) {
            throw new IllegalArgumentException("Product name was empty");
        }
        if (name.length() > 50 || name.length() < 2) {
            throw new IllegalArgumentException("Product name had wrong length");
        }
    }
}
