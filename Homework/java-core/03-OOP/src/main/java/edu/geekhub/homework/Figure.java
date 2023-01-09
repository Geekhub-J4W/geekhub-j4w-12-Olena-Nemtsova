package edu.geekhub.homework;

public abstract class Figure implements Colorful {
    protected String color;
    protected String type;
    private static int count = 1;
    protected int id;

    Figure(String type) {
        this.type = type;
        id = count++;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setColor() {
        color = getDefaultColor();
    }

    public abstract double getP();

    public abstract double getS();

    @Override
    public String toString() {
        return "Figure #" + id;
    }
}
