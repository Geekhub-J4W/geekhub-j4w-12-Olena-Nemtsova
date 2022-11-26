package edu.geekhub.orcostat.model;

public class Tank {
    private final TrivialCollection equipage;
    private static final int TANK_PRICE = 3_000_000;
    private final int price;

    public Tank(TrivialCollection equipage, int price) {
        if (equipage.count() > 6) {
            throw new IllegalArgumentException("Too many orcs");
        }

        this.price = price;
        this.equipage = equipage;
    }

    public Tank(TrivialCollection equipage) {
        this(equipage, TANK_PRICE);
    }

    public Tank() {
        this(new TrivialCollection());
    }

    public Tank(int price) {
        this(new TrivialCollection(), price);
    }

    public TrivialCollection getEquipage() {
        return equipage;
    }

    public int getPrice() {
        return price;
    }
}
