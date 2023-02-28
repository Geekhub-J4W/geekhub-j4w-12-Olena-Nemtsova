package edu.geekhub.homework.domain;

public record Category(int id, String name) {
    public Category(String name) {
        this(-1, name);
    }
}
