package edu.geekhub.coursework.usersparameters;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Aim {
    LOSE("LOSE", -400),
    GAIN("GAIN", 400),
    NONE("NONE", 0);

    private final String name;
    private final int caloriesDifference;

    Aim(String name, int caloriesDifference) {
        this.name = name;
        this.caloriesDifference = caloriesDifference;
    }

    public int getCaloriesDifference() {
        return caloriesDifference;
    }

    @JsonValue
    public String getName() {
        return name;
    }

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static Aim forValues(@JsonProperty("name") String name) {
        for (Aim aim : Aim.values()) {
            if (
                aim.name.equals(name)) {
                return aim;
            }
        }
        return null;
    }
}
