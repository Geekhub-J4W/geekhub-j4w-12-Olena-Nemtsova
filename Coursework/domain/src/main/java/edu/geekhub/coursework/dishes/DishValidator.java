package edu.geekhub.coursework.dishes;

import org.springframework.stereotype.Component;

@Component
public class DishValidator {
    public void validate(Dish dish) {
        if (dish == null) {
            throw new IllegalArgumentException("Dish was null");
        }
        validateName(dish.getName());
    }

    private void validateName(String name) {
        if (name == null) {
            throw new IllegalArgumentException("Dish name was null");
        }
        if (name.isBlank()) {
            throw new IllegalArgumentException("Dish name was empty");
        }
        if (name.length() > 50 || name.length() < 2) {
            throw new IllegalArgumentException("Dish name had wrong length");
        }
    }
}
