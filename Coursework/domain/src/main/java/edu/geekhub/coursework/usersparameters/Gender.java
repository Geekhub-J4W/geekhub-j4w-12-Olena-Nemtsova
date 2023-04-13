package edu.geekhub.coursework.usersparameters;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Gender {
    FEMALE("FEMALE"),
    MALE("MALE");

    private final String name;

    Gender(String name) {
        this.name = name;
    }

    @JsonValue
    public String getName() {
        return name;
    }

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static Gender forValues(@JsonProperty("name") String name) {
        for (Gender gender : Gender.values()) {
            if (
                gender.name.equals(name)) {
                return gender;
            }
        }
        return null;
    }
}
