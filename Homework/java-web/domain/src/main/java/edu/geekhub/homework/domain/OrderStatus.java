package edu.geekhub.homework.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum OrderStatus {
    PENDING("PENDING"),
    SHIPPED("SHIPPED"),
    COMPLETED("COMPLETED");

    private String name;

    OrderStatus(String name) {
        this.name = name;
    }

    @JsonValue
    public String getName() {
        return name;
    }

    @JsonCreator
    public static OrderStatus forValues(@JsonProperty("name") String name) {
        for (OrderStatus status : OrderStatus.values()) {
            if (
                status.name.equals(name)) {
                return status;
            }
        }
        return null;
    }
}
