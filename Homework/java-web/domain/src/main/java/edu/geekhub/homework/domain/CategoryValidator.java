package edu.geekhub.homework.domain;

public class CategoryValidator {

    public void validate(Category category) {
        if (category == null) {
            throw new IllegalArgumentException("Category was null");
        }
        validateName(category.getName());
    }

    private void validateName(String name) {
        if (name == null) {
            throw new IllegalArgumentException("Category name was null");
        }
        if (name.isBlank()) {
            throw new IllegalArgumentException("Category name was empty");
        }
        if (name.length() > 50 || name.length() < 2) {
            throw new IllegalArgumentException("Category name had wrong length");
        }
    }
}
