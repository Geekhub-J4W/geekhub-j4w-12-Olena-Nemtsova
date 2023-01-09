package edu.geekhub.homework;

public class Rectangle extends Figure {
    private final double width;
    private final double height;

    public Rectangle(double width, double height) {
        super("Rectangle");
        this.width = width;
        this.height = height;
    }

    @Override
    public double getP() {
        return (width + height) * 2;
    }

    @Override
    public double getS() {
        return width * height;
    }

    @Override
    public String toString() {
        return String.format("%s %n%s %nP = %.2f %nS = %.2f %nColor - %s %n", super.toString(), super.type, getP(), getS(), super.color);
    }
}
