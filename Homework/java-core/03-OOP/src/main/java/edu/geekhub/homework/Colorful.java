package edu.geekhub.homework;

public interface Colorful {
    default String GetColor() {
        return "black";
    }
}
