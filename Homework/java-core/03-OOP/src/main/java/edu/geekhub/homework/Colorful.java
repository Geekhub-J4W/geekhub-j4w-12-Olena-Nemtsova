package edu.geekhub.homework;

public interface Colorful {
    default String getDefaultColor() {
        return "black";
    }
}
