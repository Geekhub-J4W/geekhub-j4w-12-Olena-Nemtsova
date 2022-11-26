package edu.geekhub.orcostat.model;

public class Missile {
    private static final int MISSILE_PRICE = 4_000_000;
    private final int price;

    public Missile(int price) {
        this.price = price;
    }

    public Missile() {
        this(MISSILE_PRICE);
    }

    public int getPrice() {
        return price;
    }
}
