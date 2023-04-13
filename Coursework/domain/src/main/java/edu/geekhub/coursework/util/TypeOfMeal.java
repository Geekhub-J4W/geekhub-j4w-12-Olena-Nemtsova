package edu.geekhub.coursework.util;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

public enum TypeOfMeal {
    BREAKFAST("BREAKFAST", 0.25),
    DINNER("DINNER", 0.35),
    LUNCH("LUNCH", 0.15),
    SUPPER("SUPPER", 0.25);

    private final String name;
    private final double percentage;

    TypeOfMeal(String name, double percentage) {
        this.name = name;
        this.percentage = percentage;
    }

    public double getPercentage() {
        return percentage;
    }

    @JsonValue
    public String getName() {
        return name;
    }

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static TypeOfMeal forValues(@JsonProperty("name") String name) {
        for (TypeOfMeal typeOfMeal : TypeOfMeal.values()) {
            if (
                typeOfMeal.name.equals(name)) {
                return typeOfMeal;
            }
        }
        return null;
    }
}
