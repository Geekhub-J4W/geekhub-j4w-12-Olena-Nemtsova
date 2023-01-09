package edu.geekhub.homework;

public class Square extends Figure {
    private final double side;

    public Square(double side) {
        super("Square");
        this.side = side;
    }

    @Override
    public double getP() {
        return side * 4;
    }

    @Override
    public double getS() {
        return side * side;
    }

    @Override
    public String toString() {
        return String.format("%s %n%s %nP = %.2f %nS = %.2f %nColor - %s %n", super.toString(), super.type, getP(), getS(), super.color);
    }
}
