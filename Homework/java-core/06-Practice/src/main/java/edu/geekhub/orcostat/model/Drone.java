package edu.geekhub.orcostat.model;

public class Drone {
    private static final int DRONE_PRICE = 50_000;
    private final int price;

    public Drone(int price) {
        this.price = price;
    }

    public Drone() {
        this(DRONE_PRICE);
    }

    public int getPrice() {
        return price;
    }
}
