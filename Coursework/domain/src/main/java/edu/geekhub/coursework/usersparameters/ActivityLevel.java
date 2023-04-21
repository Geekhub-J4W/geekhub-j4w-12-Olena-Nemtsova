package edu.geekhub.coursework.usersparameters;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

public enum ActivityLevel {
    ZERO("ZERO", 1.2),
    LOW("LOW", 1.3),
    MEDIUM("MEDIUM", 1.4),
    HIGH("HIGH", 1.5);

    private final String name;
    private final double coefficient;

    ActivityLevel(String name, double coefficient) {
        this.name = name;
        this.coefficient = coefficient;
    }

    public double getCoefficient() {
        return this.coefficient;
    }

    @JsonValue
    public String getName() {
        return name;
    }

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static ActivityLevel forValues(@JsonProperty("name") String name) {
        for (ActivityLevel activityLevel : ActivityLevel.values()) {
            if (
                activityLevel.name.equals(name)) {
                return activityLevel;
            }
        }
        return null;
    }
}
