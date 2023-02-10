package edu.geekhub.homework.domain;

public class ProductValidator {

    public void validate(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("Product was null");
        }
        validateName(product.getName());
        validatePrice(product.getPrice());
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

    private void validatePrice(double price) {
        if (price <= 0) {
            throw new IllegalArgumentException("Product price was equals or less than zero");
        }

        String numbersAfterPoint = Double.toString(price).split("\\.")[1];
        if (numbersAfterPoint.length() > 2) {
            throw new IllegalArgumentException("Product price had too much numbers after point");
        }
    }
}
