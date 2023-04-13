package edu.geekhub.coursework.usersparameters;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

public enum BodyType {
    ASTHENIC("ASTHENIC"),
    NORMOSTHENIC("NORMOSTHENIC"),
    HYPERSTHENIC("HYPERSTHENIC");

    private final String name;

    BodyType(String name) {
        this.name = name;
    }

    @JsonValue
    public String getName() {
        return name;
    }

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static BodyType forValues(@JsonProperty("name") String name) {
        for (BodyType bodyType : BodyType.values()) {
            if (
                bodyType.name.equals(name)) {
                return bodyType;
            }
        }
        return null;
    }
}
